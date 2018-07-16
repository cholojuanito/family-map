package rbdavis.shared.models.http.responses;

import java.util.List;

import rbdavis.shared.models.data.Person;

/**
 * A specific find-by-id response that contains a {@code Person} model.
 *
 *
 * @see FindByIdResponse
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class PersonResponse extends FindByIdResponse<Person> {

    public PersonResponse() {
    }

    public PersonResponse(Person person) {
        super(person);
    }
}