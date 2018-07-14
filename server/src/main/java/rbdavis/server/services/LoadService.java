package rbdavis.server.services;

import rbdavis.shared.models.http.requests.LoadRequest;
import rbdavis.shared.models.http.responses.Response;

/**
 * The service that performs the load action for the "/load" endpoint.
 *
 * Each service receives a version of a Request model and returns
 * a version of a Response model. Using the Request it then interacts with
 * the corresponding {@code DAO}.
 *
 * @see Response
 * @see LoadRequest
 * @author  Tanner Davis
 * @version 0.1
 * @since   v0.1
 */

public class LoadService
{
    /**
     * Clears all data from the database and then creates rows for the
     * {@code User}, {@code Person}, and {@code Event} data from the {@code LoadRequest}.
     *
     *
     * @param request - A {@code LoadRequest} that has a {@code List} of {@code User}s, {@code Person}s and {@code Event}s
     * @return A {@code Response} object that carries the message and status code
     */
    public Response load(LoadRequest request)
    {
        return new Response("Loaded the people");
    }
}
