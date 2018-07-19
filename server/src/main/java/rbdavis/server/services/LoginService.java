package rbdavis.server.services;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.server.database.sql.dataaccess.PersonSqlDAO;
import rbdavis.server.database.sql.dataaccess.UserSqlDAO;
import rbdavis.shared.models.data.Person;
import rbdavis.shared.models.data.User;
import rbdavis.shared.models.http.requests.LoginRequest;
import rbdavis.shared.models.http.responses.LoginOrRegisterResponse;

/**
 * The service that performs the login action for the "/user/login" endpoint.
 * <p>
 * Each service receives a version of a Request model and returns
 * a version of a Response model. Using the Request it then interacts with
 * the corresponding {@code DAO}.
 *
 * @author Tanner Davis
 * @version 0.1
 * @see LoginRequest
 * @see LoginOrRegisterResponse
 * @since v0.1
 */

public class LoginService {
    /**
     * Logins in the user and returns an {@code AuthToken} inside the {@code LoginOrRegisterResponse}.
     *
     * @param request - A {@code LoadRequest} that has the username and password
     * @return A {@code LoginOrRegisterResponse} object that carries the username and {@code AuthToken}
     */
    public LoginOrRegisterResponse login(LoginRequest request) {
        SqlDatabase db;
        try {
            db = new SqlDatabase();
            // 1. Create the User model from request
            User userModel;
            // 2. Attempt to create a row in the Users table
            UserSqlDAO userDao = db.getUserDao();

            // 3. Make a Person model from the User model/request
            Person personModel;
            PersonSqlDAO personDao = db.getPersonDao();


            // 4. Make a Response and return it
        }
        catch (DAO.DatabaseException e) {
            // TODO: Log here
            // 4. Make an errorResponse and return it

            e.printStackTrace();
        }

        return new LoginOrRegisterResponse();
    }
}
