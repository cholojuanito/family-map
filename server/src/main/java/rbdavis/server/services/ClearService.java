package rbdavis.server.services;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.server.database.sql.dataaccess.AuthTokenSqlDAO;
import rbdavis.server.database.sql.dataaccess.EventSqlDAO;
import rbdavis.server.database.sql.dataaccess.PersonSqlDAO;
import rbdavis.server.database.sql.dataaccess.UserSqlDAO;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.shared.utils.Constants.CLEAR_ERR;
import static rbdavis.shared.utils.Constants.CLEAR_SUCCESS;

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

public class ClearService extends Service {
    /**
     * Deletes everything that is in the database at that moment
     *
     * @return A {@code Response} that carries the message and status code
     */
    public static Response clear() {
        Response response = new Response();
        try {
            SqlDatabase db = new SqlDatabase();
            try {
                // 1. Get all the dao's
                UserSqlDAO userDao = db.getUserDao();
                PersonSqlDAO personDao = db.getPersonDao();
                EventSqlDAO eventDao = db.getEventDao();
                AuthTokenSqlDAO authTokenDao = db.getAuthTokenDao();

                clearDatabase(userDao, personDao, eventDao, authTokenDao);

                // 3. Make a Response and return it
                db.endTransaction(true);
                response.setMessage(CLEAR_SUCCESS);
            }
            catch (DAO.DatabaseException e) {
                logger.warning(e.getMessage());
                response.setMessage(e.getMessage());
                db.endTransaction(false);
            }
            return response;
        }
        catch (DAO.DatabaseException e) {
            response.setMessage(CLEAR_ERR);
        }
        return response;
    }

    public static void clearDatabase(UserSqlDAO userDao, PersonSqlDAO personDao, EventSqlDAO eventDao, AuthTokenSqlDAO authDao) throws DAO.DatabaseException {
        userDao.deleteAll();
        personDao.deleteAll();
        eventDao.deleteAll();
        authDao.deleteAll();
    }
}
