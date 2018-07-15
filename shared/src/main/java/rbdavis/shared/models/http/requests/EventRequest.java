package rbdavis.shared.models.http.requests;

/**
 * A {@code EventRequest} is specific to the Event table
 * in the database and carries the needed information to the
 * {@code EventService} and finds the event with the
 * given id.
 *
 *
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class EventRequest extends FindByIdRequest{

    public EventRequest(String id, String token) {
        super(id, token);
        setType("Event");
    }
}
