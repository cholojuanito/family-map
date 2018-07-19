package rbdavis.server.services;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.server.database.sql.dataaccess.AuthTokenSqlDAO;
import rbdavis.server.database.sql.dataaccess.EventSqlDAO;
import rbdavis.server.database.sql.dataaccess.PersonSqlDAO;
import rbdavis.server.database.sql.dataaccess.UserSqlDAO;
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
        SqlDatabase db;
        try {
            db = new SqlDatabase();
            // 1. Get all the dao's
            UserSqlDAO userDao = db.getUserDao();
            PersonSqlDAO personDao = db.getPersonDao();
            EventSqlDAO eventSqlDao = db.getEventDao();
            AuthTokenSqlDAO authTokenDao = db.getAuthTokenDao();
            // 2. Call deleteAll on all of them


            // 3. Make a Response and return it
        }
        catch (DAO.DatabaseException e) {
            // TODO: Log here
            // 3. Make an errorResponse and return it

            e.printStackTrace();
        }
        return new Response("Everything has been cleared");
    }
}
