package rbdavis.server.services;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.shared.models.http.requests.FillRequest;
import rbdavis.shared.models.http.responses.Response;

/**
 * The service that performs the fill action for the "/fill/[username]/{generations}" endpoint.
 * <p>
 * Each service receives a version of a Request model and returns
 * a version of a Response model. Using the Request it then interacts with
 * the corresponding {@code DAO}.
 *
 * @author Tanner Davis
 * @version 0.1
 * @see Response
 * @see FillRequest
 * @since v0.1
 */

public class FillService {
    /**
     * Populates the database with generated data for the specified user.
     * The required "username" must belong to a user already registered with the server.
     * If there is any data in the database already associated with the given username, it is deleted.
     * <p>
     * Interacts with each model's version of a {@code DAO}.
     *
     * @param request - A {@code LoadRequest} that has a username
     *                and the number of generations to fill.
     * @return A {@code Response} object that carries the message and status code
     */
    public Response fill(FillRequest request) {
        SqlDatabase db;
        try {
            db = new SqlDatabase();
            // 1. Steps
        }
        catch (DAO.DatabaseException e) {
            // TODO: Log here
            // 3. Make an errorResponse and return it

            e.printStackTrace();
        }

        return new Response("Added X people to the tree");
    }
}
