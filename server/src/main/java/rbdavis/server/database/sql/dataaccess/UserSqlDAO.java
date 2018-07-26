package rbdavis.server.database.sql.dataaccess;

import java.security.cert.CRLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlConnectionManager;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.User;
import static rbdavis.shared.utils.Constants.*;

/**
 * A {@code UserSqlDAO} implements the core functionality of a {@code DAO} for the User table.
 * <p>
 * Each SQL {@code DAO} receives a {@code Connection} from the {@code SqlConnectionManager}.
 *
 * @author Tanner Davis
 * @version 0.1
 * @see DAO
 * @see SqlConnectionManager
 * @since v0.1
 */

public class UserSqlDAO extends SqlDAO implements DAO<User> {

    private static Logger logger;

    static {
        logger = Logger.getLogger("database");
    }

    private Connection connection;

    public UserSqlDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Creates a row in the User table of the database.
     *
     * @param user A {@code User} model that will be used to get values
     * @return The {@code User} model that came from the database
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public User create(User user) throws DatabaseException {
        try {
            PreparedStatement stmt = null;
            try {
                String sql = "INSERT INTO Users" +
                             "(username, person_id, password, email, first_name, last_name, gender)" +
                             "VALUES (?, ?, ?, ?, ?, ?, ?)";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPersonId());
                stmt.setString(3, user.getPassword());
                stmt.setString(4, user.getEmail());
                stmt.setString(5, user.getFirstName());
                stmt.setString(6, user.getLastName());
                stmt.setString(7, user.getGender().toString());
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
                    errorMessage = USER_ID_TAKEN;
                    break;
                default:
                    logger.severe(CREATE_USER_FAIL + e.getMessage());
                    errorMessage = CREATE_USER_FAIL;
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
        return user;
    }

    /**
     * Updates a row in the User table of the database.
     *
     * @param id The id of the row that needs to be updated
     * @param user A {@code User} model that will be used to get values
     * @return The new {@code User} model that came from the database
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public User update(String id, User user) throws DatabaseException {
        try {
            PreparedStatement stmt = null;
            try {

                String sql = "UPDATE Users " +
                             "SET password = ?, email = ?, first_name = ?, last_name = ?, gender = ?" +
                             "WHERE username = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, user.getPassword());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getFirstName());
                stmt.setString(4, user.getLastName());
                stmt.setString(5, user.getGender().toString());
                stmt.setString(6, id);
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
                    errorMessage = userIdDoesNotExist(id);
                    break;
                default:
                    logger.severe(UPDATE_USER_FAIL + e.getMessage());
                    errorMessage = UPDATE_USER_FAIL;
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
        return user;
    }

    /**
     * Deletes a row in the User table of the database.
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

                String sql = "DELETE FROM Users WHERE username = ?";
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
                    errorMessage = userIdDoesNotExist(id);
                    break;
                default:
                    logger.severe(DELETE_USER_FAIL + e.getMessage());
                    errorMessage = DELETE_USER_FAIL;
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
        return worked;
    }

    /**
     * Finds a row in the User table by id.
     *
     * @param id The id of the row to find
     * @return The {@code User} model representation that was found
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public User findById(String id) throws DatabaseException {
        User foundUser;
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {

                String sql = "SELECT * FROM Users WHERE username = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, id);

                rs = stmt.executeQuery();
                if (!rs.next()) {
                    return null;
                }
                foundUser = extractUserModel(rs);

                return foundUser;
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
                    errorMessage = userIdDoesNotExist(id);
                    break;
                default:
                    logger.severe(FIND_USER_FAIL + e.getMessage());
                    errorMessage = FIND_USER_FAIL;
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
    }

    /**
     * Finds all rows in the User table
     *
     * @return A {@code List} of {@code User} models
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public List<User> all() throws DatabaseException {
        List<User> foundUsers;
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {

                String sql = "SELECT * FROM Users";
                stmt = connection.prepareStatement(sql);

                rs = stmt.executeQuery();
                foundUsers = extractUserModels(rs);
                return foundUsers;
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
                    logger.severe(FIND_USERS_FAIL + e.getMessage());
                    errorMessage = FIND_USERS_FAIL;
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
    }

    /**
     * Deletes the whole Users table
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

                String sql = "DELETE FROM Users";
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
                    logger.severe(DELETE_USERS_FAIL + e.getMessage());
                    errorMessage = DELETE_USERS_FAIL;
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
        return worked;
    }

    private User extractUserModel(ResultSet rs) throws SQLException {
        User user;

        String userName = rs.getString(1);
        String personId = rs.getString(2);
        String password = rs.getString(3);
        String email = rs.getString(4);
        String firstName = rs.getString(5);
        String lastName = rs.getString(6);
        String genderStr = rs.getString(7);
        Gender gender;
        if (genderStr.equals("m")) {
            gender = Gender.M;
        }
        else {
            gender = Gender.F;
        }

        user = new User(userName, personId, password, email, firstName, lastName, gender);

        return user;
    }

    private List<User> extractUserModels(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();

        while (rs.next()) {
            User user = extractUserModel(rs);
            if (user != null) {
                users.add(user);
            }
        }

        return users;
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
