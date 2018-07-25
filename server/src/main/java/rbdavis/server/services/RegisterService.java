package rbdavis.server.services;

import java.util.UUID;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.server.database.sql.dataaccess.AuthTokenSqlDAO;
import rbdavis.server.database.sql.dataaccess.PersonSqlDAO;
import rbdavis.server.database.sql.dataaccess.UserSqlDAO;
import rbdavis.shared.models.data.AuthToken;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.User;
import rbdavis.shared.models.http.requests.FillRequest;
import rbdavis.shared.models.http.requests.RegisterRequest;
import rbdavis.shared.models.http.responses.LoginOrRegisterResponse;
import rbdavis.shared.models.http.responses.Response;

/**
 * The service that performs the registration action for the "/user/register" endpoint.
 * <p>
 * Each service receives a version of a Request model and returns
 * a version of a Response model. Using the Request it then interacts with
 * the corresponding {@code DAO}.
 *
 * @author Tanner Davis
 * @version 0.1
 * @see RegisterRequest
 * @see LoginOrRegisterResponse
 * @since v0.1
 */

public class RegisterService extends Service {
    SqlDatabase db = null;
    LoginOrRegisterResponse response = new LoginOrRegisterResponse();

    /**
     * Registers the user and returns an {@code AuthToken} inside the {@code LoginOrRegisterResponse}.
     *
     * @param request - A {@code RegisterRequest} that has the {@code User} info in it
     * @return A {@code LoginOrRegisterResponse} object that carries the username and {@code AuthToken}
     */
    public LoginOrRegisterResponse register(RegisterRequest request) {
        try {
            boolean commit = false;
            db = new SqlDatabase();

            // 1. Get needed DAOS
            UserSqlDAO userDao = db.getUserDao();
            AuthTokenSqlDAO authTokenDao = db.getAuthTokenDao();
            PersonSqlDAO personDao = db.getPersonDao();

            // 2. Make model from request
            String userName = request.getUserName();
            String newUserUUID = UUID.randomUUID().toString();
            Gender gender = (request.getGender().equals("m")) ? Gender.M : Gender.F;
            User userModel = new User(userName, newUserUUID, request.getPassword(), request.getEmail(),
                                      request.getFirstName(), request.getLastName(), gender);

            // 3. Create the user row in the table, if username is taken exception is caught
            userDao.create(userModel);
            User userFromDB = userDao.findById(userName);
            if (userFromDB != null) {
                logger.info("Registration successful");
                logger.info("Creating new family tree");
                AuthToken tokenModel = LoginService.createNewAuthToken(userFromDB.getUsername());
                authTokenDao.create(tokenModel);
                AuthToken tokenFromDB = authTokenDao.findById(tokenModel.getToken());

                //***************************************************************************
                // 4. Use the fill service to make a new family tree
                FillRequest fillRequest = new FillRequest(userName);
                Response fillResponse = new FillService(db).fill(fillRequest);

                logger.info(fillResponse.getMessage());

                // Send successful response back
                response.setAuthToken(tokenFromDB.getToken());
                response.setUserName(userModel.getUsername());
                response.setPersonID(userModel.getPersonId());
                response.setMessage("Success!");
                commit = true;
            }
            else {
                logger.severe("Registration unsuccessful");
                response.setMessage("Registration unsuccessful");
            }

            // Uncommented because the Fill Service will end the transaction
            //db.endTransaction(commit);
        }
        catch (DAO.DatabaseException e) {
            if (db != null) {
                try {
                    db.endTransaction(false);
                }
                catch (DAO.DatabaseException worthLessException) {
                    logger.severe("Issue closing db connection");
                }
            }
            logger.warning(e.getMessage());
            response.setMessage(e.getMessage());
        }

        return response;
    }
}
