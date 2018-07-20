package rbdavis.server.services;

import java.time.LocalDateTime;
import java.util.UUID;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.server.database.sql.dataaccess.AuthTokenSqlDAO;
import rbdavis.server.database.sql.dataaccess.UserSqlDAO;
import rbdavis.shared.models.data.AuthToken;
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
        SqlDatabase db = null;
        LoginOrRegisterResponse response = new LoginOrRegisterResponse();

        try {
            boolean commit = false;
            db = new SqlDatabase();

            // 1. Get needed DAO's
            UserSqlDAO userDao = db.getUserDao();
            AuthTokenSqlDAO authTokenDao = db.getAuthTokenDao();
            String userName = request.getUserName();

            // 2. Find username in DB... or don't
            User userFromDB = userDao.findById(request.getUserName());
            if (userFromDB == null) {
                // TODO: Log here
                response.setMessage("That username does not match our records. Please sign up first");
                commit = false;
            }
            else {
                // 3. Create a new auth token for the user
                AuthToken tokenModel = createNewAuthToken(userFromDB.getUsername());
                authTokenDao.create(tokenModel);
                AuthToken tokenFromDB = authTokenDao.findById(tokenModel.getToken());

                // 4. Send a successful response back
                response.setUserName(userFromDB.getUsername());
                response.setAuthToken(tokenFromDB.getToken());
                response.setPersonID(userFromDB.getPersonId());
                commit = true;
                // TODO: Log here
            }

            db.endTransaction(commit);
        }
        catch (DAO.DatabaseException e) {
            if (db != null) {
                try {
                    db.endTransaction(false);
                }
                catch (DAO.DatabaseException worthLessException) {
                    //TODO: Log here
                    System.out.println("Issue closing db connection");
                }
            }
            // TODO: Log here
            response.setMessage(e.getMessage());
        }

        return response;
    }

    public static AuthToken createNewAuthToken(String userName) {
        LocalDateTime startTime = LocalDateTime.now();
        String tokenUUID = UUID.randomUUID().toString();
        return new AuthToken(tokenUUID, userName, startTime);
    }
}
