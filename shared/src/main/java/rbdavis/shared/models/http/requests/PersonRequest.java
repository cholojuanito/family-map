package rbdavis.shared.models.http.requests;

/**
 * A {@code PersonRequest} is specific to the Person table
 * in the database and carries the needed information to the
 * {@code PersonService} and finds the person with the
 * given id.
 *
 *
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class PersonRequest extends FindByIdRequest {

    public PersonRequest(String id, String token) {
        super(id, token);
        setType("Person");
    }


}
