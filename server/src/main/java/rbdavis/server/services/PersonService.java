package rbdavis.server.services;

import java.util.List;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.server.database.sql.dataaccess.AuthTokenSqlDAO;
import rbdavis.server.database.sql.dataaccess.PersonSqlDAO;
import rbdavis.server.database.sql.dataaccess.UserSqlDAO;
import rbdavis.shared.models.data.AuthToken;
import rbdavis.shared.models.data.Person;
import rbdavis.shared.models.data.User;
import rbdavis.shared.models.http.requests.PeopleRequest;
import rbdavis.shared.models.http.requests.PersonRequest;
import rbdavis.shared.models.http.responses.PeopleResponse;
import rbdavis.shared.models.http.responses.PersonResponse;
import static rbdavis.shared.utils.Constants.*;

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

    /**
     * Uses the {@code PersonSqlDAO} to find
     * all rows in the Persons table of the database
     *
     * @param request A request to find all rows in the database
     * @return A response that has a {@code List} of {@code Person}s
     */
    public PeopleResponse findAllPeople(PeopleRequest request) {
        PeopleResponse response = new PeopleResponse();
        try {
            boolean commit = false;
            db = new SqlDatabase();
            PersonSqlDAO personDao = db.getPersonDao();
            AuthTokenSqlDAO authDao = db.getAuthTokenDao();

            AuthToken currUserToken =  authDao.findById(request.getToken());
            List<Person> peopleFromDB = personDao.findByUsername(currUserToken.getUserId());
            if (peopleFromDB == null || peopleFromDB.size() == 0) {
                response.setMessage(NO_RECORDS_ERR);
            }
            else {
                response.setData(peopleFromDB);
                response.setMessage(SUCCESS);
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
                    logger.severe(DB_CLOSE_ERR);
                }
            }
            logger.warning(e.getMessage());
            response.setMessage(e.getMessage());
        }
        return response;
    }

    /**
     * Uses the {@code PersonSqlDAO} to find the row
     * in the Events table of the database with the given id
     *
     * @param request A request to find the id in the database
     * @return A response that has an {@code Person}
     */
    public PersonResponse findPerson(PersonRequest request) {
        PersonResponse response = new PersonResponse();
        try {
            boolean commit = false;
            db = new SqlDatabase();
            PersonSqlDAO personDao = db.getPersonDao();
            AuthTokenSqlDAO authDao = db.getAuthTokenDao();

            AuthToken currUserToken =  authDao.findById(request.getToken());
            Person personFromDB = personDao.findById(request.getId());
            if (personFromDB == null) {
                String noSuchPerson = "No person with id " + request.getId() + " exists";
                response.setMessage(noSuchPerson);
            }
            else if (!personFromDB.getUserId().equals(currUserToken.getUserId())) {
                response.setMessage("This person " + NOT_THEIRS);
            }
            else {
                response.setData(personFromDB);
                response.setMessage(SUCCESS);
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
                    logger.severe(DB_CLOSE_ERR);
                }
            }
            logger.warning(e.getMessage());
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
