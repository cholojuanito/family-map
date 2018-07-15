package rbdavis.server.services;

import rbdavis.shared.models.http.requests.EventRequest;
import rbdavis.shared.models.http.requests.EventsRequest;
import rbdavis.shared.models.http.responses.EventResponse;
import rbdavis.shared.models.http.responses.EventsResponse;

/**
 * The service that performs the actions for the "/event"
 * & the "/event/[id]" endpoints.
 * <p>
 * Each service receives a version of a Request model and returns
 * a version of a Response model. Using the Request it then interacts with
 * the corresponding {@code DAO}.
 *
 * @author Tanner Davis
 * @version 0.1
 * @see rbdavis.shared.models.http.responses.FindAllResponse
 * @see rbdavis.shared.models.http.responses.FindByIdResponse
 * @since v0.1
 */
public class EventService {

    /**
     * Uses the {@code EventSqlDAO} to find
     * all rows in the Events table of the database
     *
     * @param request A request to find all rows in the database
     * @return A response that has a {@code List} of {@code Event}s
     */
    public EventsResponse findAll(EventsRequest request) {
        return new EventsResponse();
    }

    /**
     * Uses the {@code EventSqlDAO} to find the row
     * in the Events table of the database with the given id
     *
     * @param request A request to find the id in the database
     * @return A response that has an {@code Event}
     */
    public EventResponse findById(EventRequest request) {
        return new EventResponse();
    }
}
