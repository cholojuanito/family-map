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

    private T data;

    public FindByIdResponse() {
    }

    public FindByIdResponse(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
