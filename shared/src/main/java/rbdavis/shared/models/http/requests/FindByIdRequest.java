package rbdavis.shared.models.http.requests;

/**
 * A generic find-by-id request {@code Request} mimics a HTTP request and carries information
 * from the client that made the API call to the  then
 * to the .
 *
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class FindByIdRequest {
    private String type;
    private String id;
    private String token;

    public FindByIdRequest() {
    }

    public FindByIdRequest(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public FindByIdRequest(String type, String id, String token) {
        this(id, token);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
