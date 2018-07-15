package rbdavis.shared.models.http.requests;

/**
 * A {@code PeopleRequest} is specific to the Person table
 * in the database and carries the needed information to the
 * {@code PersonService} and finds all the people.
 *
 *
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class PeopleRequest extends FindAllRequest {

    public PeopleRequest(String token) {
        super(token);
        setType("People");
    }

}
