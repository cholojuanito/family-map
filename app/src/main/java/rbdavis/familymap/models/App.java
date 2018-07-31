package rbdavis.familymap.models;

import java.util.List;
import java.util.Map;
import java.util.Set;

import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Person;
import rbdavis.shared.models.data.User;

public class App {
    /*Singleton*/
    private static App _familyMap;

    public static App getInstance() {
        return _familyMap;
    }

    static {
        _familyMap =  new App();
    }


    private Map<String, User> people;
    private Map<String, Event> events;
    private Map<String, List<Event>> personalEvents;
    // Settings
    // Filter
    private Set<String> eventTypes;
    private Map<String, MapPinColor> typeColors;

    private Person user;
    private Set<String> paternalAncestors;
    private Set<String> maternalAncestors;
    private Map<String, List<Person>> childrenOfPerson;




    public Map<String, User> getPeople() {
        return people;
    }

    public void setPeople(Map<String, User> people) {
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

    public Map<String, MapPinColor> getTypeColors() {
        return typeColors;
    }

    public void setTypeColors(Map<String, MapPinColor> typeColors) {
        this.typeColors = typeColors;
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
