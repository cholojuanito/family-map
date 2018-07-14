package rbdavis.shared.models.http.requests;

/**
 * A specific {@code Request} Encapsulates a HTTP request and carries information
 * from the client that made the API call to the {@code LoginHandler} then
 * to the {@code LoginService}.
 * <p>
 * It contains the username and password of the user that
 * wants to login
 *
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class LoginRequest {
    private String userName;
    private String password;

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
