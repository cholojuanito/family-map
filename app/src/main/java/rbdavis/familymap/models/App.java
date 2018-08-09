package rbdavis.familymap.models;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Person;

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
    // Filters

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
        // Filters
        focusedPersonId = null;
        eventTypes = new HashSet<>();
        eventTypeColors = new HashMap<>();
        markersToEvents = new HashMap<>();
        eventsToMarkers = new HashMap<>();
        personMarkers = new HashMap<>();
        connections = new ArrayList<>();
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

    public Map<String, Person> getPeople() {
        return people;
    }

    public void setPeople(Map<String, Person> people) {
        this.people = people;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Event> events) {
        this.events = events;
    }

    public Map<String, List<Event>> getPersonalEvents() {
        return personalEvents;
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

    public Set<String> getPaternalAncestors() {
        return paternalAncestors;
    }

    public void setPaternalAncestors() {
        String fatherId = people.get(focusedPersonId).getFatherId();
        paternalAncestors.add(focusedPersonId);
        findAncestors(fatherId, paternalAncestors);
    }

    public void setPaternalAncestors(Set<String> paternalAncestors) {
        this.paternalAncestors = paternalAncestors;
    }

    public Set<String> getMaternalAncestors() {
        return maternalAncestors;
    }

    public void setMaternalAncestors() {
        String motherId = people.get(focusedPersonId).getMotherId();
        maternalAncestors.add(focusedPersonId);
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
}
