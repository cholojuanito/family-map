package rbdavis.shared.models.http.requests;

import java.util.List;

import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Person;
import rbdavis.shared.models.data.User;

public class LoadRequest
{
    private List<User> users;
    private List<Person> people;
    private List<Event> events;
}
