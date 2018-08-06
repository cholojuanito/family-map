package rbdavis.familymap.models;

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
    // Settings
    // Filters
    private Set<String> eventTypes;
    private Map<String, MapMarkerColor> eventTypeColors;

    private App() {
        people = new HashMap<>();
        events = new HashMap<>();
        personalEvents = new HashMap<>();
        userPersonId = null;
        user = null;
        paternalAncestors = new HashSet<>();
        maternalAncestors =  new HashSet<>();
        childrenOfPerson = new HashMap<>();
        // Settings
        // Filters
        eventTypes = new HashSet<>();
        eventTypeColors = new HashMap<>();
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

    public void setPaternalAncestors(Set<String> paternalAncestors) {
        this.paternalAncestors = paternalAncestors;
    }

    public Set<String> getMaternalAncestors() {
        return maternalAncestors;
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
}
