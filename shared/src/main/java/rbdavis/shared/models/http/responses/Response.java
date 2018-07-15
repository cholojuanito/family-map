package rbdavis.shared.models.http.responses;

/**
 * A {@code Response} encapsulates a HTTP response and carries information
 * from a {@code Serivce} to the corresponding {@code Handler}
 * and back to the client that made the API call.
 * <p>
 * By default it contains a message and a status code.
 *
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class Response {
    private String message;
    private int statusCode;

    public Response() {
    }

    public Response(String message) {
        this.message = message;
    }

    public Response(String message, int statusCode) {
        this(message);
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
