package rbdavis.server.database.sql.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlConnectionManager;
import rbdavis.shared.models.data.AuthToken;

/**
 * A {@code AuthTokenSqlDAO} implements the core functionality of a {@code DAO} for the AuthToken table.
 * <p>
 * Each SQL {@code DAO} receives a {@code Connection} from the {@code SqlConnectionManager}.
 *
 * @author Tanner Davis
 * @version 0.1
 * @see DAO
 * @see SqlConnectionManager
 * @since v0.1
 */

public class AuthTokenSqlDAO implements DAO<AuthToken> {
    private Connection connection;
    private final DateTimeFormatter TOKEN_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public AuthTokenSqlDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Creates a row in the AuthToken table of the database.
     *
     * @param token A {@code Token} model that will be used to get values
     * @return The {@code Token} model that came from the database
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public AuthToken create(AuthToken token) throws DatabaseException {
        try {
            PreparedStatement stmt = null;
            try {
                String sql = "INSERT INTO AuthTokens" +
                        "(token, user_id, start_time, end_time)" +
                        "VALUES (?, ?, ?, ?)";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, token.getToken());
                stmt.setString(2, token.getUserId());
                stmt.setString(3, token.getStartTime().format(TOKEN_FORMATTER));
                stmt.setString(4, token.getEndTime().format(TOKEN_FORMATTER));

//                if (stmt.executeUpdate() == 1) {
//                    connection.commit();
//                }
//                else {
//                    connection.rollback();
//                }
            }
            finally {
                closeStatement(stmt);
            }
        }
        catch (SQLException e) {
            String errorMessage;
            switch (e.getErrorCode()) {
                case 1:
                    errorMessage = "Invalid SQL syntax.";
                    break;
                case 19:
                    errorMessage = "Id is already taken.";
                    break;
                default:
                    errorMessage = "Create auth token failed.";
                    break;
            }
            throw new DAO.DatabaseException(errorMessage, e);
        }
        return token;
    }

    /**
     * Updates a row in the AuthToken table of the database.
     *
     * @param id The id of the row that needs to be updated
     * @param token A {@code AuthToken} model that will be used to get values
     * @return The new {@code AuthToken} model that came from the database
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public AuthToken update(String id, AuthToken token) throws DatabaseException {
        return null;
    }

    /**
     * Deletes a row in the AuthToken table of the database.
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

                String sql = "DELETE FROM AuthTokens WHERE id = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, id);

                if (stmt.executeUpdate() == 1) {
                    //connection.commit();
                    worked = true;
                }
//                else {
//                    connection.rollback();
//                }
            }
            finally {
                closeStatement(stmt);
            }
        }
        catch (SQLException e) {
            throw new DAO.DatabaseException("deleteAuthToken failed ", e);
        }
        return worked;
    }

    /**
     * Finds a row in the AuthToken table by id.
     *
     * @param user_id The id of the row to find
     * @return The {@code AuthToken} model representation that was found
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public AuthToken findById(String user_id) throws DatabaseException {
        AuthToken token;
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {

                String sql = "SELECT * FROM AuthTokens WHERE id = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, user_id);

                rs = stmt.executeQuery();
                rs.next();
                token = extractTokenModel(rs);

                return token;
            }
            finally {
                closeStatement(stmt);
                closeResultSet(rs);
            }
        }
        catch (SQLException e) {
            throw new DAO.DatabaseException("findAuthTokenById failed ", e);
        }
    }

    /**
     * Finds all rows in the AuthToken table
     *
     * @return A {@code List} of {@code AuthToken} models
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public List<AuthToken> all() throws DatabaseException {
        List<AuthToken> foundTokens = null;
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {

                String sql = "SELECT * FROM AuthTokens";
                stmt = connection.prepareStatement(sql);

                rs = stmt.executeQuery();
                foundTokens = extractTokenModels(rs);
                return foundTokens;
            }
            finally {
                closeStatement(stmt);
                closeResultSet(rs);
            }
        }
        catch (SQLException e) {
            throw new DAO.DatabaseException("findAllUsers failed ", e);
        }
    }

    /**
     * Finds all rows in the AuthToken table by userId.
     *
     * @param userId The foreign key used to find the rows
     * @return The {@code AuthToken} models that were found
     * @throws DatabaseException Any issue with the database is thrown
     */
    public AuthToken findByUserId(String userId) throws DatabaseException {
        List<AuthToken> foundTokens = null;
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {

                String sql = "SELECT * FROM AuthTokens WHERE user_id = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, userId);

                rs = stmt.executeQuery();
                foundTokens = extractTokenModels(rs);

                return findCurrentToken(foundTokens);
            }
            finally {
                closeStatement(stmt);
                closeResultSet(rs);
            }
        }
        catch (SQLException e) {
            throw new DAO.DatabaseException("findAllAuthTokens failed ", e);
        }
    }

    private AuthToken extractTokenModel(ResultSet rs) throws SQLException {
        AuthToken tokenModel = null;

        String token = rs.getString(1);
        String userId = rs.getString(2);
        LocalDateTime start = LocalDateTime.parse(rs.getString(3));
        LocalDateTime end = LocalDateTime.parse(rs.getString(4));

        tokenModel = new AuthToken(token, userId, start, end);

        return tokenModel;
    }

    private List<AuthToken> extractTokenModels(ResultSet rs) throws SQLException {
        List<AuthToken> tokens = new ArrayList<>();

        while (rs.next()) {
            AuthToken token = extractTokenModel(rs);
            if (token != null) {
                tokens.add(token);
            }
        }

        return tokens;
    }

    private AuthToken findCurrentToken(List<AuthToken> tokens) {
        for (AuthToken token: tokens) {
            if (!token.isExpired()) {
                return token;
            }
        }
        return null;
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
