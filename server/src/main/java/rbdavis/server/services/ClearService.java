package rbdavis.server.services;

import rbdavis.shared.models.http.responses.Response;

/**
 * The service that performs the clear action for the "/clear" endpoint.
 *
 * Each service receives a version of a Request model and returns
 * a version of a Response model. Using the Request it then interacts with
 * the corresponding {@code DAO}.
 *
 * @see Response
 * @author  Tanner Davis
 * @version 0.1
 * @since   v0.1
 */

public class ClearService
{
    /**
     * Deletes everything that is in the database at that moment
     *
     * @return A {@code Response} that carries the message and status code
     */
    public Response clear()
    {
        return new Response("Everything has been cleared");
    }
}
