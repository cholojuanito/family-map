package rbdavis.server.database;

import java.util.List;

import rbdavis.server.database.sql.dataaccess.AuthTokenSqlDAO;
import rbdavis.server.database.sql.dataaccess.EventSqlDAO;
import rbdavis.server.database.sql.dataaccess.PersonSqlDAO;
import rbdavis.server.database.sql.dataaccess.UserSqlDAO;

/**
 * This is a generic Database Access Object (DAO) interface.
 * <p>
 * It defines the core functionality of the {@code DAO}'s that implement it.
 * The methods are just basic CRUD operations on the database.
 * <p>
 * Each individual DAO implementation interacts with that specific
 * table in the database.
 *
 * @param <T> Type of data model
 * @author Tanner Davis
 * @version 0.1
 * @see UserSqlDAO
 * @see EventSqlDAO
 * @see PersonSqlDAO
 * @see AuthTokenSqlDAO
 * @since v0.1
 */

public interface DAO<T> {
    /**
     * Creates a row in the database based on the model type
     *
     * @param type The model type to interact with
     * @return The created model from the SQL statement
     * @throws DatabaseException Any issues with database queries
     */
    T create(T type) throws DatabaseException;

    /**
     * Updates a row in the database based on the id and type of object
     *
     * @param id   The id of the row to update
     * @param type The model type to interact with
     * @return The newly updated model from the SQL statement
     * @throws DatabaseException Any issues with database queries
     */
    T update(String id, T type) throws DatabaseException;

    /**
     * Deletes a row in the database based on the id
     *
     * @param id The id of the row to delete
     * @return True if the row was there and was deleted. False otherwise.
     * @throws DatabaseException Any issues with database queries
     */
    boolean delete(String id) throws DatabaseException;

    /**
     * Finds a row in the database based on the id
     *
     * @param id The id of the row to find
     * @return The model that was found by the SQL statement
     * @throws DatabaseException Any issues with database queries
     */
    T findById(String id) throws DatabaseException;

    /**
     * Gets all rows in a particular table of the database.
     *
     * @return A {@code List} of the model type
     * @throws DatabaseException Any issues with database queries
     */
    List<T> all() throws DatabaseException;

    /**
     * Deletes the whole database table
     *
     * @return True if things were deleted. False otherwise.
     * @throws DatabaseException Any issues with database queries
     */
    boolean deleteAll() throws DatabaseException;


    /**
     * An exception to help with recognizing database issues.
     * This way you don't have to deal with only {@code SQLExceptions}.
     * It inherits directly from the {@code Exception} class.
     */
    class DatabaseException extends Exception {
        public DatabaseException() {
            super();
        }

        public DatabaseException(String message) {
            super(message);
        }

        public DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }

        public DatabaseException(Throwable cause) {
            super(cause);
        }
    }
}
