package rbdavis.shared.models.http.requests;

import java.util.List;

import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Person;
import rbdavis.shared.models.data.User;

/**
 * A specific {@code Request} Encapsulates a HTTP request and carries information
 * from the client that made the API call to the {@code LoadHandler} then
 * to the {@code LoadService}.
 * <p>
 * It contains:
 * <ul>
 * <li>{@code List} of {@code User}s</li>
 * <li>{@code List} of {@code Person}s</li>
 * <li>{@code List} of {@code Event}s</li>
 * </ul>
 *
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class LoadRequest {
    private List<User> users;
    private List<Person> people;
    private List<Event> events;

    public LoadRequest(List<User> users, List<Person> people, List<Event> events) {
        this.users = users;
        this.people = people;
        this.events = events;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
