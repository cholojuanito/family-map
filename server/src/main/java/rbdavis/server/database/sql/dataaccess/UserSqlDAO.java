package rbdavis.server.database.sql.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlConnectionFactory;
import rbdavis.shared.models.data.User;

/**
 * A {@code UserSqlDAO} implements the core functionality of a {@code DAO} for the User table.
 * <p>
 * Each SQL{@code DAO} receives a {@code Connection} from the {@code SqlConnectionFactory}.
 *
 * @author Tanner Davis
 * @version 0.1
 * @see DAO
 * @see SqlConnectionFactory
 * @since v0.1
 */

public class UserSqlDAO implements DAO<User> {
    private Connection connection = null;

    /**
     * Creates a row in the User table of the database.
     *
     * @param user - A {@code User} model that will be used to get values
     * @return The {@code User} model that came from the database
     * @throws DatabaseException - Any issue with the database is thrown
     */
    @Override
    public User create(User user) throws DatabaseException {
        try {
            connection = SqlConnectionFactory.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {

                String sql = "INSERT INTO " + user.TABLE +
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

                if (stmt.executeUpdate() != 1) {
                    throw new DAO.DatabaseException("Create user failed.");
                } else {
                    connection.commit();
                }
            } finally {
                if (stmt != null) {
                    stmt.close();
                }

                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            throw new DAO.DatabaseException("createUser failed " + e.getMessage(), e);
        }
        return user;
    }

    /**
     * Updates a row in the User table of the database.
     *
     * @param id   - The id of the row that needs to be updated
     * @param user - A {@code User} model that will be used to get values
     * @return The new {@code User} model that came from the database
     * @throws DatabaseException - Any issue with the database is thrown
     */
    @Override
    public User update(String id, User user) throws DatabaseException {
        return user;
    }

    /**
     * Deletes a row in the User table of the database.
     *
     * @param id - The id of the row that needs to be deleted
     * @return True if a row was deleted, false otherwise
     * @throws DatabaseException - Any issue with the database is thrown
     */
    @Override
    public boolean delete(String id) throws DatabaseException {
        return false;
    }

    /**
     * Finds a row in the User table by id.
     *
     * @param id - The id of the row to find
     * @return The {@code User} model representation that was found
     * @throws DatabaseException - Any issue with the database is thrown
     */
    @Override
    public User findById(String id) throws DatabaseException {
        return null;
    }

    /**
     * Finds all rows in the User table
     *
     * @return A {@code List} of {@code User} models
     * @throws DatabaseException - Any issue with the database is thrown
     */
    @Override
    public List<User> all() throws DatabaseException {
        return null;
    }
}
