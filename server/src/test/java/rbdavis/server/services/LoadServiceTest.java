package rbdavis.server.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Person;
import rbdavis.shared.models.data.User;
import rbdavis.shared.models.http.requests.LoadRequest;
import rbdavis.shared.models.http.responses.Response;

import static org.junit.Assert.assertEquals;

public class LoadServiceTest {

    private LoadService serviceUnderTest;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Person> people = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        serviceUnderTest = new LoadService();
    }

    @After
    public void tearDown() throws Exception {
        serviceUnderTest = null;
    }

    @Test
    public void testLoad() {
        addUsers();
        addPeople();
        addEvents();

        String expectedResponse = "Added " + users.size() + " users " + people.size() + " people and " + events.size() + " events to database";

        LoadRequest req = new LoadRequest(users, people, events);
        Response response = serviceUnderTest.load(req);
        assertEquals(expectedResponse, response.getMessage());
    }

    private void addUsers() {
        users.add(new User("juls", "1", "secret", "email@me", "Julianne", "Capito", "F"));
        users.add(new User("cholo", "4", "pass", "email@me", "Tanner", "Davis", "M"));
    }

    private void addPeople() {
        people.add(new Person("1", "juls", "Julianne", "Capito", "F", "2", "3", null));
        people.add(new Person("2", "juls", "Michael", "Capito", "M", null, null, "3"));
        people.add(new Person("3", "juls", "Sharon", "Halsebo", "F", null, null, "2"));
        people.add(new Person("4", "cholo", "Tanner", "Davis", "M", "6", "5", null));
        people.add(new Person("5", "cholo", "Barb", "Henderson", "F", null, null, "6"));
        people.add(new Person("6", "cholo", "Rod", "Davis", "M", null, null, "5"));
    }

    private void addEvents() {
        events.add(new Event("1", "1", "juls", "BIRTH", "41.5833",
                             "-92.3833", "Des Moines", "United States", LocalDate.of(1993, 2, 17)));
        events.add(new Event("2", "2", "juls", "BIRTH", "41.5833",
                             "-92.3833", "Des Moines", "United States", LocalDate.of(1958, 12, 25)));
        events.add(new Event("3", "3", "juls", "BIRTH", "41.5833",
                             "-92.3833", "Des Moines", "United States", LocalDate.of(1961, 9, 9)));
        events.add(new Event("4", "4", "cholo", "BIRTH", "41.5833",
                             "-92.3833", "American Fork", "United States", LocalDate.of(1994, 9, 5)));
        events.add(new Event("5", "5", "cholo", "BIRTH", "41.5833",
                             "-92.3833", "Salt Lake City", "United States", LocalDate.of(1960, 4, 11)));
        events.add(new Event("6", "6", "cholo", "BIRTH", "41.5833",
                             "-92.3833", "Twin Falls", "United States", LocalDate.of(1961, 2, 9)));
    }
}