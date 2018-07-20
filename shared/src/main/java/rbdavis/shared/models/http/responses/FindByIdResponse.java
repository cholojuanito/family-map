package rbdavis.shared.models.http.responses;

/**
 * A base find-by-id response contains the specific model type.
 *
 * @param <T> Type of data model
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class FindByIdResponse<T> extends Response {

    private T type;

    public FindByIdResponse() {
    }

    public FindByIdResponse(T type) {
        this.type = type;
    }

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }
}
