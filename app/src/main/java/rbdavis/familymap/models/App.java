package rbdavis.familymap.models;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rbdavis.familymap.net.http.ServerProxy;
import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.Person;
import rbdavis.shared.models.http.responses.EventsResponse;
import rbdavis.shared.models.http.responses.PeopleResponse;
import rbdavis.shared.utils.Constants;

public class App {
    /*Singleton*/
    private static App _familyMap;

    public static App getInstance() {
        return _familyMap;
    }

    static {
        _familyMap =  new App();
    }

    private Map<String, Person> people;
    private Map<String, Event> events;
    private Map<String, List<Event>> personalEvents;
    private String userPersonId;
    private Person user;
    private Set<String> paternalAncestors;
    private Set<String> maternalAncestors;
    private Map<String, List<Person>> childrenOfPerson;
    private Settings settings;
    private Filters filters;

    // Map data
    private String focusedPersonId;
    private Set<String> eventTypes;
    private Map<String, MapMarkerColor> eventTypeColors;
    private Map<Marker, String> markersToEvents;
    private Map<String, Marker> eventsToMarkers;
    private Map<Marker, String> personMarkers;
    private List<Polyline> connections;

    private App() {
        people = new HashMap<>();
        events = new HashMap<>();
        personalEvents = new HashMap<>();
        userPersonId = null;
        user = null;
        paternalAncestors = new HashSet<>();
        maternalAncestors =  new HashSet<>();
        childrenOfPerson = new HashMap<>();
        settings = new Settings();
        filters = new Filters();
        focusedPersonId = null;
        eventTypes = new HashSet<>();
        eventTypeColors = new HashMap<>();
        markersToEvents = new HashMap<>();
        eventsToMarkers = new HashMap<>();
        personMarkers = new HashMap<>();
        connections = new LinkedList<>();
    }

    private void findAncestors(String id, Set<String> ancestors) {
        ancestors.add(id);

        if (people.get(id).getFatherId() != null) {
            findAncestors(people.get(id).getFatherId(), ancestors);
        }

        if (people.get(id).getMotherId() != null) {
            findAncestors(people.get(id).getMotherId(), ancestors);
        }
    }

    public void addChildToParent(Person child) {
        if (child.getFatherId() != null) {
            if (childrenOfPerson.containsKey(child.getFatherId())) {
                childrenOfPerson.get(child.getFatherId()).add(child);
            }
            else {
                // Todo maybe make it a LinkedList
                List<Person> children = new ArrayList<>();

                children.add(child);
                childrenOfPerson.put(child.getFatherId(), children);
            }
        }

        if (child.getMotherId() != null) {
            if (childrenOfPerson.containsKey(child.getMotherId())) {
                childrenOfPerson.get(child.getMotherId()).add(child);
            }
            else {
                // Todo maybe make it a LinkedList
                List<Person> children = new ArrayList<>();

                children.add(child);
                childrenOfPerson.put(child.getMotherId(), children);
            }
        }

    }

    public void sortPersonalEvents() {
        for (Map.Entry<String, List<Event>> entry : personalEvents.entrySet()) {
            Collections.sort(entry.getValue());
        }
    }

    public void performLogout() {
        ServerProxy.getInstance().logout();
        resetModel(false);
    }

    public void resetModel(boolean keepSettings) {

        for (MapMarkerColor color : MapMarkerColor.values()) {
            color.setIsUsed(false);
        }

        people = new HashMap<>();
        events = new HashMap<>();
        personalEvents = new HashMap<>();
        userPersonId = null;
        user = null;
        paternalAncestors = new HashSet<>();
        maternalAncestors =  new HashSet<>();
        childrenOfPerson = new HashMap<>();
        if (!keepSettings) {
            settings = new Settings();
        }
        filters = new Filters();
        focusedPersonId = null;
        eventTypes = new HashSet<>();
        eventTypeColors = new HashMap<>();
        markersToEvents = new HashMap<>();
        eventsToMarkers = new HashMap<>();
        personMarkers = new HashMap<>();
        connections = new LinkedList<>();
    }

    public void resetLines() {

        for (Polyline line : connections) {
            line.remove();
        }

        connections.clear();
    }

    public Map<String, Person> getPeople() {
        return people;
    }

    public void setPeople(PeopleResponse peopleResponse) {

        for (Person p : peopleResponse.getData()) {
            String id = p.getId();
            if (id.equals(getUserPersonId())) {
                setUser(p);
            }

            people.put(id, p);
            addChildToParent(p);
        }
    }

    public void setPeople(Map<String, Person> people) {
        this.people = people;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public Map<String, Event> getFilteredEvents() {
        Map<String, Event> filteredEvents = filterByFamilySide();

        filterByEventType(filteredEvents);

        return filteredEvents;
    }

    private Map<String, Event> filterByFamilySide() {
        Map<String, Event> filteredEvents = new HashMap<>();
        boolean fatherSideCanBeShown = filters.getFilterOptions().get(Constants.BY_FATHER_SIDE);
        boolean motherSideCanBeShown = filters.getFilterOptions().get(Constants.BY_MOTHER_SIDE);

        if (fatherSideCanBeShown && motherSideCanBeShown) {
            // Make a copy of all the events
            filteredEvents = new HashMap<>(events);
        }
        else if (!fatherSideCanBeShown && motherSideCanBeShown) {
            filteredEvents = gatherMotherSideEvents();
        }
        else if (fatherSideCanBeShown && !motherSideCanBeShown) {
            filteredEvents = gatherFatherSideEvents();
        }

        // Add user's events
        for (Event userEvent : personalEvents.get(userPersonId)) {
            filteredEvents.put(userEvent.getId(), userEvent);
        }

        return filteredEvents;
    }

    private Map<String, Event> gatherFatherSideEvents() {
        Map<String, Event> events = new HashMap<>();
        for (String personId : paternalAncestors) {
            for (Event e : personalEvents.get(personId)) {
                events.put(e.getId(), e);
            }
        }
        return events;
    }

    private Map<String, Event> gatherMotherSideEvents() {
        Map<String, Event> events = new HashMap<>();
        for (String personId : maternalAncestors) {
            for (Event e : personalEvents.get(personId)) {
                events.put(e.getId(), e);
            }
        }
        return events;
    }

    private void filterByEventType(Map<String, Event> filteredEvents) {

        Map<String, Event> copyMap = new HashMap<>(filteredEvents);

        for (Map.Entry<String, Event> entry : copyMap.entrySet()) {
            Event e = entry.getValue();
            boolean eventCanBeShown = filters.getFilterOptions().get(e.getEventType());

            if (eventCanBeShown) {
                filterByGender(filteredEvents, e);
            }
            else {
                filteredEvents.remove(e.getId());
            }
        }
    }

    private void filterByGender(Map<String, Event> filteredEvents, Event event) {
        boolean maleCanBeShown = filters.getFilterOptions().get(Constants.BY_MALE);
        boolean femaleCanBeShown = filters.getFilterOptions().get(Constants.BY_FEMALE);


        if (!maleCanBeShown && !femaleCanBeShown) {
            filteredEvents.remove(event.getId());
        }
        else if (!maleCanBeShown && femaleCanBeShown) {
            if (people.get(event.getPersonId()).getGender() == Gender.M) {
                filteredEvents.remove(event.getId());
            }
        }
        else if (maleCanBeShown && !femaleCanBeShown) {
            if (people.get(event.getPersonId()).getGender() == Gender.F) {
                filteredEvents.remove(event.getId());
            }
        }
    }

    public void setEvents(EventsResponse eventsResponse) {

        for (Event e : eventsResponse.getData()) {
            String id = e.getId();
            getEvents().put(id, e);

            // Gather all the event types while we are at it
            String eventType = e.getEventType();
            getEventTypes().add(eventType);
        }

        addEventTypesToFilters();
    }

    public void setEvents(Map<String, Event> events) {
        this.events = events;
    }

    public Map<String, List<Event>> getPersonalEvents() {
        return personalEvents;
    }

    public void setPersonalEvents() {

        for (Map.Entry<String, Event> entry : getEvents().entrySet()) {
            Event e = entry.getValue();
            String personId = e.getPersonId();

            List<Event> personsEvents = personalEvents.get(personId);
            if (personsEvents == null) {
                personsEvents = new LinkedList<>();
                personsEvents.add(e);
                personalEvents.put(personId, personsEvents);
            }
            else {
                personsEvents.add(e);
            }
        }

        sortPersonalEvents();
    }

    public void setPersonalEvents(Map<String, List<Event>> personalEvents) {
        this.personalEvents = personalEvents;
    }

    public Set<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(Set<String> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public Map<String, MapMarkerColor> getEventTypeColors() {
        return eventTypeColors;
    }

    public void setEventTypeColors() {

        for (String type : eventTypes) {
            for (MapMarkerColor color : MapMarkerColor.values()) {
                if (!color.isUsed()) {
                    color.setIsUsed(true);
                    getEventTypeColors().put(type, color);
                    break;
                }
            }
        }
    }

    public void setEventTypeColors(Map<String, MapMarkerColor> eventTypeColors) {
        this.eventTypeColors = eventTypeColors;
    }

    public String getUserPersonId() {
        return userPersonId;
    }

    public void setUserPersonId(String userPersonId) {
        this.userPersonId = userPersonId;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public String getFocusedPersonId() {
        return focusedPersonId;
    }

    public void setFocusedPersonId(String focusedPersonId) {
        this.focusedPersonId = focusedPersonId;
    }

    public Set<String> getPaternalAncestors() {
        return paternalAncestors;
    }

    public void setPaternalAncestors() {
        String fatherId = people.get(userPersonId).getFatherId();
        paternalAncestors.add(userPersonId);
        findAncestors(fatherId, paternalAncestors);
    }

    public void setPaternalAncestors(Set<String> paternalAncestors) {
        this.paternalAncestors = paternalAncestors;
    }

    public Set<String> getMaternalAncestors() {
        return maternalAncestors;
    }

    public void setMaternalAncestors() {
        String motherId = people.get(userPersonId).getMotherId();
        maternalAncestors.add(userPersonId);
        findAncestors(motherId, maternalAncestors);
    }

    public void setMaternalAncestors(Set<String> maternalAncestors) {
        this.maternalAncestors = maternalAncestors;
    }

    public Map<String, List<Person>> getChildrenOfPerson() {
        return childrenOfPerson;
    }

    public void setChildrenOfPerson(Map<String, List<Person>> childrenOfPerson) {
        this.childrenOfPerson = childrenOfPerson;
    }

    public Map<Marker, String> getMarkersToEvents() {
        return markersToEvents;
    }

    public void setMarkersToEvents(Map<Marker, String> markersToEvents) {
        this.markersToEvents = markersToEvents;
    }

    public Map<String, Marker> getEventsToMarkers() {
        return eventsToMarkers;
    }

    public void setEventsToMarkers(Map<String, Marker> eventsToMarkers) {
        this.eventsToMarkers = eventsToMarkers;
    }

    public Map<Marker, String> getPersonMarkers() {
        return personMarkers;
    }

    public void setPersonMarkers(Map<Marker, String> personMarkers) {
        this.personMarkers = personMarkers;
    }

    public List<Polyline> getConnections() {
        return connections;
    }

    public void setConnections(List<Polyline> connections) {
        this.connections = connections;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void addEventTypesToFilters() {
        for (String type : eventTypes) {
            filters.getFilterOptions().put(type, true);
        }
    }

    public boolean hasFilters() {
        for (Map.Entry<String, Boolean> entry : filters.getFilterOptions().entrySet()) {
            if (!entry.getValue()) {
                return true;
            }
        }
        return false;
    }

    public Filters getFilters() {
        return filters;
    }

    public void setFilters(Filters filters) {
        this.filters = filters;
    }
}
