package rbdavis.shared.models.http.responses;

import rbdavis.shared.models.data.Event;

/**
 * A specific find-by-id response that contains an {@code Event} model.
 *
 * @author Tanner Davis
 * @version 0.1
 * @see FindByIdResponse
 * @since v0.1
 */

public class EventResponse extends FindByIdResponse<Event> {

    public EventResponse() {
    }

    public EventResponse(Event type) {
        super(type);
    }
}
