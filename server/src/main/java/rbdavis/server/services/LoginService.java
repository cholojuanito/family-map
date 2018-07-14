package rbdavis.server.services;

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
        return new LoginOrRegisterResponse();
    }
}
