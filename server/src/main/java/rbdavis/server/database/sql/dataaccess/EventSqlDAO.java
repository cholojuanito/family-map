package rbdavis.server.database.sql.dataaccess;

import java.sql.Connection;
import java.util.List;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlConnectionManager;
import rbdavis.shared.models.data.Event;

/**
 * A {@code EventSqlDAO} implements the core functionality of a {@code DAO} for the Event table.
 * <p>
 * Each SQL {@code DAO} receives a {@code Connection} from the {@code SqlConnectionManager}.
 *
 * @author Tanner Davis
 * @version 0.1
 * @see DAO
 * @see SqlConnectionManager
 * @since v0.1
 */

public class EventSqlDAO implements DAO<Event> {
    private Connection connection = null;

    /**
     * Creates a row in the Event table of the database.
     *
     * @param event A {@code Event} model that will be used to get values
     * @return The {@code Event} model that came from the database
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public Event create(Event event) throws DatabaseException {
        return event;
    }

    /**
     * Updates a row in the Event table of the database.
     *
     * @param id The id of the row that needs to be updated
     * @param event A {@code Event} model that will be used to get values
     * @return The new {@code Event} model that came from the database
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public Event update(String id, Event event) throws DatabaseException {
        return event;
    }

    /**
     * Deletes a row in the Event table of the database.
     *
     * @param id The id of the row that needs to be deleted
     * @return True if a row was deleted, false otherwise
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public boolean delete(String id) throws DatabaseException {
        return false;
    }

    /**
     * Finds a row in the Event table by id.
     *
     * @param id The id of the row to find
     * @return The {@code Event} model representation that was found
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public Event findById(String id) throws DatabaseException {
        return null;
    }

    /**
     * Finds all rows in the Event table
     *
     * @return A {@code List} of {@code Event} models
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public List<Event> all() throws DatabaseException {
        return null;
    }

    /**
     * Uses the username to find all {@code Event}s associated with that user.
     *
     * @param username the foreign key used for finding {@code Event}s
     * @return A {@code List} of {@code Event} models
     */
    public List<Event> findByUsername(String username) {
        return null;
    }


    /**
     * Uses the username to delete all {@code Event}s associated with that user.
     *
     * @param username the foreign key used for deleting
     * @return True if anything was deleted. False otherwise.
     */
    public boolean deleteByUsername(String username) {
        return false;
    }
}
