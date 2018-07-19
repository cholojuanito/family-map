package rbdavis.server.services;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.server.database.sql.dataaccess.AuthTokenSqlDAO;
import rbdavis.server.database.sql.dataaccess.EventSqlDAO;
import rbdavis.server.database.sql.dataaccess.PersonSqlDAO;
import rbdavis.server.database.sql.dataaccess.UserSqlDAO;
import rbdavis.shared.models.http.requests.LoadRequest;
import rbdavis.shared.models.http.responses.Response;

/**
 * The service that performs the load action for the "/load" endpoint.
 * <p>
 * Each service receives a version of a Request model and returns
 * a version of a Response model. Using the Request it then interacts with
 * the corresponding {@code DAO}.
 *
 * @author Tanner Davis
 * @version 0.1
 * @see Response
 * @see LoadRequest
 * @since v0.1
 */

public class LoadService {
    /**
     * Clears all data from the database and then creates rows for the
     * {@code User}, {@code Person}, and {@code Event} data from the {@code LoadRequest}.
     *
     * @param request - A {@code LoadRequest} that has a {@code List} of {@code User}s, {@code Person}s and {@code Event}s
     * @return A {@code Response} object that carries the message and status code
     */
    public Response load(LoadRequest request) {
        SqlDatabase db;
        try {
            db = new SqlDatabase();
            // 1. Get needed Dao's
            UserSqlDAO userDao = db.getUserDao();
            PersonSqlDAO personDao = db.getPersonDao();
            EventSqlDAO eventDao = db.getEventDao();
            AuthTokenSqlDAO authTokenDao = db.getAuthTokenDao();


            // 3. Make a Response and return it
        }
        catch (DAO.DatabaseException e) {
            // TODO: Log here
            // 3. Make an errorResponse and return it

            e.printStackTrace();
        }
        return new Response("Loaded the people");
    }
}
