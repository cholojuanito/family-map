package rbdavis.server.services;

import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.shared.models.http.requests.RegisterRequest;
import rbdavis.shared.models.http.responses.LoginOrRegisterResponse;

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

public class RegisterService {
    /**
     * Registers the user and returns an {@code AuthToken} inside the {@code LoginOrRegisterResponse}.
     *
     * @param request - A {@code RegisterRequest} that has the {@code User} info in it
     * @return A {@code LoginOrRegisterResponse} object that carries the username and {@code AuthToken}
     */
    public LoginOrRegisterResponse register(RegisterRequest request) {
        SqlDatabase db = new SqlDatabase();

        return new LoginOrRegisterResponse();
    }
}
