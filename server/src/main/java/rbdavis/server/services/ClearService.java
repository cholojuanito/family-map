package rbdavis.server.services;

import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.shared.models.http.responses.Response;

/**
 * The service that performs the clear action for the "/clear" endpoint.
 * <p>
 * Each service receives a version of a Request model and returns
 * a version of a Response model. Using the Request it then interacts with
 * the corresponding {@code DAO}.
 *
 * @author Tanner Davis
 * @version 0.1
 * @see Response
 * @since v0.1
 */

public class ClearService {
    /**
     * Deletes everything that is in the database at that moment
     *
     * @return A {@code Response} that carries the message and status code
     */
    public Response clear() {
        SqlDatabase db = new SqlDatabase();
        return new Response("Everything has been cleared");
    }
}
