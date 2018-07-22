package rbdavis.server.services;

import java.util.List;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.server.database.sql.dataaccess.PersonSqlDAO;
import rbdavis.shared.models.data.Person;
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

public class PersonService extends Service {
    SqlDatabase db = null;
    PersonResponse response = new PersonResponse();

    /**
     * Uses the {@code PersonSqlDAO} to find
     * all rows in the Persons table of the database
     *
     * @param request A request to find all rows in the database
     * @return A response that has a {@code List} of {@code Person}s
     */
    public PeopleResponse findAllPeople(PeopleRequest request) {

        try {
            boolean commit = false;
            db = new SqlDatabase();
            // 1. Get needed Dao's
            PersonSqlDAO personDao = db.getPersonDao();
            // 2. Call all on dao
            List<Person> people;

            // 3. Make a Response and return it
        }
        catch (DAO.DatabaseException e) {
            // TODO: Log here
            // 3. Make an errorResponse and return it

            e.printStackTrace();
        }
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
        try {
            boolean commit = false;
            db = new SqlDatabase();
            PersonSqlDAO personDao = db.getPersonDao();

            Person personFromDB = personDao.findById(request.getId());
            if (personFromDB == null) {
                logger.warning("Query for Person with id " + request.getId() + " unsuccessful");
                response.setMessage("The id " + request.getId() + " does not match our records");
            }
            else {
                logger.info("Query for Person with id " + request.getId() + " successful");
                response.setData(personFromDB);
                commit = true;
            }
            db.endTransaction(commit);
        }
        catch (DAO.DatabaseException e) {
            if (db != null) {
                try {
                    db.endTransaction(false);
                }
                catch (DAO.DatabaseException worthLessException) {
                    logger.severe("Issue closing db connection");
                }
            }
            logger.warning(e.getMessage());
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
