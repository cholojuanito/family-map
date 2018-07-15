package rbdavis.shared.models.http.requests;

/**
 * A base find-all request {@code Request} mimics a HTTP request and carries information
 * needed for finding all rows of that type in the database;
 *
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class FindAllRequest {

    private String type;
    private String token;

    public FindAllRequest() {
    }

    public FindAllRequest(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }
}
