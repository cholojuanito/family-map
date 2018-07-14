package rbdavis.server.services;

import rbdavis.shared.models.http.requests.RegisterRequest;
import rbdavis.shared.models.http.responses.LoginOrRegisterResponse;

/**
 * The service that performs the registration action for the "/user/register" endpoint.
 *
 * Each service receives a version of a Request model and returns
 * a version of a Response model. Using the Request it then interacts with
 * the corresponding {@code DAO}.
 *
 * @see RegisterRequest
 * @see LoginOrRegisterResponse
 * @author  Tanner Davis
 * @version 0.1
 * @since   v0.1
 */

public class RegisterService
{
    /**
     * Registers the user and returns an {@code AuthToken} inside the {@code LoginOrRegisterResponse}.
     *
     * @param request - A {@code RegisterRequest} that has the {@code User} info in it
     * @return A {@code LoginOrRegisterResponse} object that carries the username and {@code AuthToken}
     */
    public LoginOrRegisterResponse register(RegisterRequest request)
    {
        return new LoginOrRegisterResponse();
    }
}
