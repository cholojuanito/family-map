package rbdavis.server.services;

import org.sqlite.core.DB;

import java.util.ArrayList;
import java.util.List;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.server.database.sql.dataaccess.*;
import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Person;
import rbdavis.shared.models.data.User;
import rbdavis.shared.models.http.requests.LoadRequest;
import rbdavis.shared.models.http.responses.Response;
import static rbdavis.shared.utils.Constants.*;

/**
 * The service that performs the load action for the "/load" endpoint.
 * <p>
 * Each service receives a version of a Request model and returns
 * a version of a Response model. Using the Request it then interacts with
 * the corresponding {@code DAO}.
 *
 * @author Tanner Davis
 * @version 0.1
 * @see Response
 * @see LoadRequest
 * @since v0.1
 */

public class LoadService extends Service {
    private SqlDatabase db = null;

    /**
     * Clears all data from the database and then creates rows for the
     * {@code User}, {@code Person}, and {@code Event} data from the {@code LoadRequest}.
     *
     * @param request - A {@code LoadRequest} that has a {@code List} of {@code User}s, {@code Person}s and {@code Event}s
     * @return A {@code Response} object that carries the message and status code
     */
    public Response load(LoadRequest request) {
        boolean commit = false;
        Response response = new Response();
        int numUsersAdded = request.getUsers().size();
        int numPeopleAdded = request.getPeople().size();
        int numEventsAdded = request.getEvents().size();
        String successResponse = "Added " + numUsersAdded + " users " + numPeopleAdded + " people and " + numEventsAdded + " events to database";

        try {
            db = new SqlDatabase();
            UserSqlDAO userDao = db.getUserDao();
            PersonSqlDAO personDao = db.getPersonDao();
            EventSqlDAO eventDao = db.getEventDao();
            AuthTokenSqlDAO authTokenDao = db.getAuthTokenDao();

            ClearService.clearDatabase(userDao, personDao, eventDao, authTokenDao);
            createUsers(userDao, request.getUsers());
            createPeople(personDao, request.getPeople());
            createEvents(eventDao, request.getEvents());

            commit = true;
            db.endTransaction(commit);
            response.setMessage(successResponse);
        }
        catch (DAO.DatabaseException e) {
            if (db != null) {
                try {
                    db.endTransaction(commit);
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

    private void createUsers(UserSqlDAO userDao, List<User> users) throws DAO.DatabaseException {
        for (User u : users) {
            userDao.create(u);
        }
    }

    private void createPeople(PersonSqlDAO personDao, List<Person> people) throws DAO.DatabaseException {
        for (Person p : people) {
            personDao.create(p);
        }
    }

    private void createEvents(EventSqlDAO eventDao, List<Event> events) throws DAO.DatabaseException {
        for (Event e : events) {
            eventDao.create(e);
        }
    }
}
