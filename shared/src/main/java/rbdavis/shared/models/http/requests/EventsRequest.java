package rbdavis.shared.models.http.requests;

/**
 * A {@code EventsRequest} is specific to the Person table
 * in the database and carries the needed information to the
 * {@code EventService} and finds all the events.
 *
 *
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class EventsRequest extends FindAllRequest {

    public EventsRequest(String token) {
        super(token);
        setType("Events");
    }
}
