package rbdavis.shared.models.http.responses;

import java.util.List;

import rbdavis.shared.models.data.Person;

/**
 * A specific find-all response that contains a {@code Person} model.
 *
 * @see FindAllResponse
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class PeopleResponse extends FindAllResponse<Person> {

    public PeopleResponse() {
    }

    public PeopleResponse(List<Person> data) {
        super(data);
    }
}
