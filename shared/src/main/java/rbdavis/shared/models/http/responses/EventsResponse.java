package rbdavis.shared.models.http.responses;

import java.util.List;

import rbdavis.shared.models.data.Event;

/**
 * A specific find-all response that contains an {@code Event} model.
 *
 * @see FindAllResponse
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class EventsResponse extends FindAllResponse<Event> {

    public EventsResponse() {
    }

    public EventsResponse(List<Event> data) {
        super(data);
    }
}
