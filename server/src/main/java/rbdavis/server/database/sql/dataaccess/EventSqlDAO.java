package rbdavis.server.database.sql.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlConnectionManager;
import rbdavis.shared.models.data.Event;

import static rbdavis.shared.utils.Constants.CREATE_EVENT_FAIL;
import static rbdavis.shared.utils.Constants.DELETE_BY_USER_EVENT_FAIL;
import static rbdavis.shared.utils.Constants.DELETE_EVENTS_FAIL;
import static rbdavis.shared.utils.Constants.DELETE_EVENT_FAIL;
import static rbdavis.shared.utils.Constants.EVENT_ID_TAKEN;
import static rbdavis.shared.utils.Constants.FIND_BY_PERSON_EVENTS_FAIL;
import static rbdavis.shared.utils.Constants.FIND_BY_USER_EVENT_FAIL;
import static rbdavis.shared.utils.Constants.FIND_EVENTS_FAIL;
import static rbdavis.shared.utils.Constants.FIND_EVENT_FAIL;
import static rbdavis.shared.utils.Constants.INVALID_SQL;
import static rbdavis.shared.utils.Constants.UPDATE_EVENT_FAIL;

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

public class EventSqlDAO extends SqlDAO implements DAO<Event> {
    private static Logger logger;

    static {
        logger = Logger.getLogger("database");
    }

    private Connection connection;
    private final DateTimeFormatter EVENT_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public EventSqlDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Creates a row in the Event table of the database.
     *
     * @param event A {@code Event} model that will be used to get values
     * @return The {@code Event} model that came from the database
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public Event create(Event event) throws DatabaseException {
        try {
            PreparedStatement stmt = null;
            try {
                String sql = "INSERT INTO Events " +
                        "(id, person_id, user_id, type, latitude, longitude, city, country, date_happened) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, event.getId());
                stmt.setString(2, event.getPersonId());
                stmt.setString(3, event.getUserId());
                stmt.setString(4, event.getEventType());
                stmt.setString(5, event.getLatitude());
                stmt.setString(6, event.getLongitude());
                stmt.setString(7, event.getCity());
                stmt.setString(8, event.getCountry());
                stmt.setString(9, event.getDateHappened().format(EVENT_FORMATTER));
                stmt.executeUpdate();
            }
            finally {
                closeStatement(stmt);
            }
        }
        catch (SQLException e) {
            String errorMessage;
            switch (e.getErrorCode()) {
                case 1:
                    errorMessage = INVALID_SQL;
                    break;
                case 19:
                    errorMessage = EVENT_ID_TAKEN;
                    break;
                default:
                    logger.severe(CREATE_EVENT_FAIL + e.getMessage());
                    errorMessage = CREATE_EVENT_FAIL;
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
        return event;
    }

    /**
     * Updates a row in the Event table of the database.
     *
     * @param id    The id of the row that needs to be updated
     * @param event A {@code Event} model that will be used to get values
     * @return The new {@code Event} model that came from the database
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public Event update(String id, Event event) throws DatabaseException {
        try {
            PreparedStatement stmt = null;
            try {

                String sql = "UPDATE Events " +
                        "SET type = ?, latitude = ?, longitude = ?, city = ?," +
                        "country = ?, date_happened = ? " +
                        "WHERE id = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, event.getEventType());
                stmt.setString(2, event.getLatitude());
                stmt.setString(3, event.getLongitude());
                stmt.setString(4, event.getCity());
                stmt.setString(5, event.getCountry());
                stmt.setString(6, event.getDateHappened().format(EVENT_FORMATTER));
                stmt.setString(7, id);
                stmt.executeUpdate();
            }
            finally {
                closeStatement(stmt);
            }
        }
        catch (SQLException e) {
            String errorMessage;
            int errorCode = e.getErrorCode();
            switch (errorCode) {
                case 1:
                    errorMessage = INVALID_SQL;
                    break;
                case 19:
                    errorMessage = eventDoesNotExist(id);
                    break;
                default:
                    logger.severe(UPDATE_EVENT_FAIL + e.getMessage());
                    errorMessage = UPDATE_EVENT_FAIL;
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
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
        boolean worked = false;
        try {
            PreparedStatement stmt = null;
            try {

                String sql = "DELETE FROM Events WHERE id = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, id);

                if (stmt.executeUpdate() == 1) {
                    worked = true;
                }
            }
            finally {
                closeStatement(stmt);
            }
        }
        catch (SQLException e) {
            String errorMessage;
            switch (e.getErrorCode()) {
                case 1:
                    errorMessage = INVALID_SQL;
                    break;
                case 19:
                    errorMessage = eventDoesNotExist(id);
                    break;
                default:
                    logger.severe(DELETE_EVENT_FAIL + e.getMessage());
                    errorMessage = DELETE_EVENT_FAIL;
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
        return worked;
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
        Event foundEvent;
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {

                String sql = "SELECT * FROM Events WHERE id = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, id);

                rs = stmt.executeQuery();
                if (!rs.next()) {
                    return null;
                }
                foundEvent = extractEventModel(rs);

                return foundEvent;
            }
            finally {
                closeStatement(stmt);
                closeResultSet(rs);
            }
        }
        catch (SQLException e) {
            String errorMessage;
            switch (e.getErrorCode()) {
                case 1:
                    errorMessage = INVALID_SQL;
                    break;
                case 19:
                    errorMessage = eventDoesNotExist(id);
                    break;
                default:
                    logger.severe(FIND_EVENT_FAIL + e.getMessage());
                    errorMessage = FIND_EVENT_FAIL;
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
    }

    /**
     * Finds all rows in the Event table
     *
     * @return A {@code List} of {@code Event} models
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public List<Event> all() throws DatabaseException {
        List<Event> foundEvents;
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {

                String sql = "SELECT * FROM Events";
                stmt = connection.prepareStatement(sql);

                rs = stmt.executeQuery();
                foundEvents = extractEventModels(rs);
                return foundEvents;
            }
            finally {
                closeStatement(stmt);
                closeResultSet(rs);
            }
        }
        catch (SQLException e) {
            String errorMessage;
            switch (e.getErrorCode()) {
                case 1:
                    errorMessage = INVALID_SQL;
                    break;
                default:
                    logger.severe(FIND_EVENTS_FAIL + e.getMessage());
                    errorMessage = FIND_EVENTS_FAIL;
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
    }

    /**
     * Uses the username to find all {@code Event}s associated with that user.
     *
     * @param username the foreign key used for finding {@code Event}s
     * @return A {@code List} of {@code Event} models
     */
    public List<Event> findByUsername(String username) throws DatabaseException {
        List<Event> foundEvents;
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {

                String sql = "SELECT * FROM Events WHERE user_id = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, username);

                rs = stmt.executeQuery();
                foundEvents = extractEventModels(rs);

                return foundEvents;
            }
            finally {
                closeStatement(stmt);
                closeResultSet(rs);
            }
        }
        catch (SQLException e) {
            String errorMessage;
            switch (e.getErrorCode()) {
                case 1:
                    errorMessage = INVALID_SQL;
                    break;
                case 19:
                    errorMessage = userIdDoesNotExist(username);
                    break;
                default:
                    logger.severe(FIND_BY_USER_EVENT_FAIL + e.getMessage());
                    errorMessage = FIND_BY_USER_EVENT_FAIL;
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
    }

    /**
     * Deletes the whole Events table
     *
     * @return True if things were deleted. False otherwise.
     * @throws DatabaseException Any issues with database queries
     */
    @Override
    public boolean deleteAll() throws DatabaseException {
        boolean worked = false;
        try {
            PreparedStatement stmt = null;
            try {

                String sql = "DELETE FROM Events";
                stmt = connection.prepareStatement(sql);

                if (stmt.executeUpdate() >= 1) {
                    worked = true;
                }
            }
            finally {
                closeStatement(stmt);
            }
        }
        catch (SQLException e) {
            String errorMessage;
            switch (e.getErrorCode()) {
                case 1:
                    errorMessage = INVALID_SQL;
                    break;
                default:
                    logger.severe(DELETE_EVENTS_FAIL + e.getMessage());
                    errorMessage = DELETE_EVENTS_FAIL;
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
        return worked;
    }


    /**
     * Uses the username to delete all {@code Event}s associated with that user.
     *
     * @param username the foreign key used for deleting
     * @return True if anything was deleted. False otherwise.
     */
    public boolean deleteByUsername(String username) throws DatabaseException {
        boolean worked = false;
        try {
            PreparedStatement stmt = null;
            try {
                String sql = "DELETE FROM Events WHERE user_id = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, username);

                if (stmt.executeUpdate() >= 1) {
                    worked = true;
                }
            }
            finally {
                closeStatement(stmt);
            }
        }
        catch (SQLException e) {
            String errorMessage;
            switch (e.getErrorCode()) {
                case 1:
                    errorMessage = INVALID_SQL;
                    break;
                case 19:
                    errorMessage = userIdDoesNotExist(username);
                    break;
                default:
                    logger.severe(DELETE_BY_USER_EVENT_FAIL + e.getMessage());
                    errorMessage = DELETE_BY_USER_EVENT_FAIL;
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
        return worked;
    }

    /**
     * Uses a person's id to find all {@code Event}s associated with that person.
     *
     * @param personId the foreign key used for finding {@code Event}s
     * @return A {@code List} of {@code Event} models
     */
    public List<Event> findByPersonId(String personId) throws DatabaseException {
        List<Event> foundEvents;
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {

                String sql = "SELECT * FROM Events WHERE person_id = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, personId);

                rs = stmt.executeQuery();
                foundEvents = extractEventModels(rs);

                return foundEvents;
            }
            finally {
                closeStatement(stmt);
                closeResultSet(rs);
            }
        }
        catch (SQLException e) {
            String errorMessage;
            switch (e.getErrorCode()) {
                case 1:
                    errorMessage = INVALID_SQL;
                    break;
                case 19:
                    errorMessage = personIdDoesNotExist(personId);
                    break;
                default:
                    logger.severe(FIND_BY_PERSON_EVENTS_FAIL + e.getMessage());
                    errorMessage = FIND_BY_PERSON_EVENTS_FAIL;
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
    }

    private Event extractEventModel(ResultSet rs) throws SQLException {
        Event event;

        String id = rs.getString(1);
        String personId = rs.getString(2);
        String userId = rs.getString(3);
        String typeStr = rs.getString(4);
        String latitude = rs.getString(5);
        String longitude = rs.getString(6);
        String city = rs.getString(7);
        String country = rs.getString(8);
        LocalDate date;
        try {
            date = LocalDate.parse(rs.getString(9), EVENT_FORMATTER);
        }
        catch (NullPointerException | DateTimeParseException e) {
            date = null;
        }

        event = new Event(id, personId, userId, typeStr, latitude, longitude, city, country, date);

        return event;
    }

    private List<Event> extractEventModels(ResultSet rs) throws SQLException {
        List<Event> events = new ArrayList<>();

        while (rs.next()) {
            Event event = extractEventModel(rs);
            if (event != null) {
                events.add(event);
            }
        }

        return events;
    }

    private void closeStatement(Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }

    private void closeResultSet(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }
}
