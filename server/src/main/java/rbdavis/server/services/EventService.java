package rbdavis.server.services;

import java.util.List;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.server.database.sql.dataaccess.EventSqlDAO;
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
public class EventService {

    /**
     * Uses the {@code EventSqlDAO} to find
     * all rows in the Events table of the database
     *
     * @param request A request to find all rows in the database
     * @return A response that has a {@code List} of {@code Event}s
     */
    public EventsResponse findAllEvents(EventsRequest request) {
        SqlDatabase db;
        try {
            db = new SqlDatabase();
            // 1. Get needed Dao's
            EventSqlDAO eventDao = db.getEventDao();
            // 2. Call all on dao
            List<Event> events;

            // 3. Make a Response and return it
        }
        catch (DAO.DatabaseException e) {
            // TODO: Log here
            // 3. Make an errorResponse and return it

            e.printStackTrace();
        }

        return new EventsResponse();
    }

    /**
     * Uses the {@code EventSqlDAO} to find the row
     * in the Events table of the database with the given id
     *
     * @param request A request to find the id in the database
     * @return A response that has an {@code Event}
     */
    public EventResponse findEvent(EventRequest request) {
        SqlDatabase db;
        try {
            db = new SqlDatabase();
            // 1. Get needed Dao's
            EventSqlDAO eventDao = db.getEventDao();
            // 2. Call getById on dao
            Event event;

            // 3. Make a Response and return it
        }
        catch (DAO.DatabaseException e) {
            // TODO: Log here
            // 4. Make an errorResponse and return it

            e.printStackTrace();
        }

        return new EventResponse();
    }
}
