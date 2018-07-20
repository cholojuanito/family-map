package rbdavis.shared.models.http.responses;

import java.util.List;

/**
 * A base find-all response contains a {@code List} of the
 * specific model type.
 *
 * @param <T> Type of data model
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class FindAllResponse<T> extends Response {

    private List<T> data;

    public FindAllResponse() {
    }

    public FindAllResponse(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
