package rbdavis.shared.models.http.requests;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Person;
import rbdavis.shared.models.data.User;

/**
 * A specific {@code Request} mimics a HTTP request and carries information
 * from the client that made the API call to the {@code LoadHandler} then
 * to the {@code LoadService}.
 * <p>
 * It contains:
 * <ul>
 * <li>{@code ArrayList} of {@code User}s</li>
 * <li>{@code ArrayList} of {@code Person}s</li>
 * <li>{@code ArrayList} of {@code Event}s</li>
 * </ul>
 *
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class LoadRequest {
    private ArrayList<User> users;
    @SerializedName("persons")
    private ArrayList<Person> people;
    private ArrayList<Event> events;

    public LoadRequest(ArrayList<User> users, ArrayList<Person> people, ArrayList<Event> events) {
        this.users = users;
        this.people = people;
        this.events = events;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<Person> people) {
        this.people = people;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
