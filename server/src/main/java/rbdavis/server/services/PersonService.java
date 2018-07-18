package rbdavis.server.services;

import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.shared.models.http.requests.PeopleRequest;
import rbdavis.shared.models.http.requests.PersonRequest;
import rbdavis.shared.models.http.responses.PeopleResponse;
import rbdavis.shared.models.http.responses.PersonResponse;

/**
 * The service that performs the actions for the "/person"
 * & the "/person/[id]" endpoints.
 * <p>
 * Each service receives a version of a Request model and returns
 * a version of a Response model. Using the Request it then interacts with
 * the corresponding {@code DAO}.
 *
 * @author Tanner Davis
 * @version 0.1
 * @see rbdavis.shared.models.http.responses.FindAllResponse
 * @see rbdavis.shared.models.http.responses.FindByIdResponse
 * @since v0.1
 */

public class PersonService {

    /**
     * Uses the {@code PersonSqlDAO} to find
     * all rows in the Persons table of the database
     *
     * @param request A request to find all rows in the database
     * @return A response that has a {@code List} of {@code Person}s
     */
    public PeopleResponse findAllPeople(PeopleRequest request) {
        SqlDatabase db = new SqlDatabase();
        return new PeopleResponse();
    }

    /**
     * Uses the {@code PersonSqlDAO} to find the row
     * in the Events table of the database with the given id
     *
     * @param request A request to find the id in the database
     * @return A response that has an {@code Person}
     */
    public PersonResponse findPerson(PersonRequest request) {
        SqlDatabase db = new SqlDatabase();
        return new PersonResponse();
    }
}
