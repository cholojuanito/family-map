package rbdavis.server.database.dataaccess.sql;

import rbdavis.server.database.dataaccess.DAO;
import rbdavis.shared.models.AuthToken;

import java.sql.Connection;
import java.util.List;

/**
 * A {@code AuthTokenSqlDAO} implements the core functionality of a {@code DAO} for the AuthToken table.
 *
 * Each SQL{@code DAO} receives a {@code Connection} from the {@code SqlConnectionFactory}.
 *
 * @see rbdavis.server.database.dataaccess.DAO
 * @see rbdavis.server.database.dataaccess.sql.SqlConnectionFactory
 * @author  Tanner Davis
 * @version 0.1
 * @since   v0.1
 */

public class AuthTokenSqlDAO implements DAO<AuthToken>
{
    private Connection connection = null;

    /**
     * Creates a row in the AuthToken table of the database.
     *
     * @param token - A {@code Token} model that will be used to get values
     * @return The {@code Token} model that came from the database
     * @throws DatabaseException - Any issue with the database is thrown
     */
    @Override
    public AuthToken create(AuthToken token) throws DatabaseException
    {
        return token;
    }

    /**
     * Updates a row in the AuthToken table of the database.
     *
     * @param id - The id of the row that needs to be updated
     * @param token - A {@code AuthToken} model that will be used to get values
     * @return The new {@code AuthToken} model that came from the database
     * @throws DatabaseException - Any issue with the database is thrown
     */
    @Override
    public AuthToken update(String id, AuthToken token) throws DatabaseException
    {
        return token;
    }

    /**
     * Deletes a row in the AuthToken table of the database.
     *
     * @param id - The id of the row that needs to be deleted
     * @return True if a row was deleted, false otherwise
     * @throws DatabaseException - Any issue with the database is thrown
     */
    @Override
    public boolean delete(String id) throws DatabaseException
    {
        return false;
    }

    /**
     * Finds a row in the AuthToken table by id.
     *
     * @param id - The id of the row to find
     * @return The {@code AuthToken} model representation that was found
     * @throws DatabaseException - Any issue with the database is thrown
     */
    @Override
    public AuthToken findById(String id) throws DatabaseException
    {
        return null;
    }

    /**
     * Finds all rows in the AuthToken table
     *
     * @return A {@code List} of {@code AuthToken} models
     * @throws DatabaseException - Any issue with the database is thrown
     */
    @Override
    public List<AuthToken> all() throws DatabaseException
    {
        return null;
    }
}
