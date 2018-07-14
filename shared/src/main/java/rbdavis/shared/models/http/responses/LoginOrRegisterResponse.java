package rbdavis.shared.models.http.responses;

/**
 * A {@code Response} subclass that is specific to
 * the login or register API calls.
 * <p>
 * By default it contains an {@code AuthToken}, a username and the person's ID.
 *
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class LoginOrRegisterResponse extends Response {
    String authToken;
    String userName;
    String personID;

    public LoginOrRegisterResponse() {
        super();
    }

    public LoginOrRegisterResponse(String message, String authToken, String userName, String personID) {
        super(message);
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
