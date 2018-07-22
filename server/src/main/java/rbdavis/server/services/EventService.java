package rbdavis.server.services;

import java.util.List;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.server.database.sql.dataaccess.AuthTokenSqlDAO;
import rbdavis.server.database.sql.dataaccess.EventSqlDAO;
import rbdavis.shared.models.data.AuthToken;
import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.http.requests.EventRequest;
import rbdavis.shared.models.http.requests.EventsRequest;
import rbdavis.shared.models.http.responses.EventResponse;
import rbdavis.shared.models.http.responses.EventsResponse;

/**
 * The service that performs the actions for the "/event"
 * & the "/event/[id]" endpoints.
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
public class EventService extends Service {
    SqlDatabase db = null;

    /**
     * Uses the {@code EventSqlDAO} to find
     * all rows in the Events table of the database
     *
     * @param request A request to find all rows in the database
     * @return A response that has a {@code List} of {@code Event}s
     */
    public EventsResponse findAllEvents(EventsRequest request) {
        EventsResponse response = new EventsResponse();
        try {
            boolean commit = false;
            db = new SqlDatabase();
            EventSqlDAO eventDao = db.getEventDao();
            AuthTokenSqlDAO authDao = db.getAuthTokenDao();

            AuthToken currUserToken =  authDao.findById(request.getToken());
            List<Event> eventsFromDB = eventDao.findByUsername(currUserToken.getUserId());
            if (eventsFromDB == null) {
                logger.warning("Query for all Event's unsuccessful");
                response.setMessage("Unable to find any records");
            }
            else if (eventsFromDB.size() == 0) {
                response.setMessage("You have no events saved in the database");
            }
            else {
                logger.info("Query for all Event's successful");
                response.setData(eventsFromDB);
                response.setMessage("Success!");
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

    /**
     * Uses the {@code EventSqlDAO} to find the row
     * in the Events table of the database with the given id
     *
     * @param request A request to find the id in the database
     * @return A response that has an {@code Event}
     */
    public EventResponse findEvent(EventRequest request) {
        EventResponse response = new EventResponse();
        try {
            boolean commit = false;
            db = new SqlDatabase();
            EventSqlDAO eventDao = db.getEventDao();

            Event eventFromDB = eventDao.findById(request.getId());
            if (eventFromDB == null) {
                logger.warning("Query for Event with id " + request.getId() + " unsuccessful");
                response.setMessage("An Event with id " + request.getId() + " does not exist");
            }
            else {
                logger.info("Query for Event with id " + request.getId() + " unsuccessful");
                response.setData(eventFromDB);
                response.setMessage("Success!");
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
