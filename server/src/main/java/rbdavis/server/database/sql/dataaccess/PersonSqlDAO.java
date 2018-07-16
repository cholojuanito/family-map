package rbdavis.server.database.sql.dataaccess;

import java.sql.Connection;
import java.util.List;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlConnectionManager;
import rbdavis.shared.models.data.Person;

/**
 * A {@code PersonSqlDAO} implements the core functionality of a {@code DAO} for the Person table.
 * <p>
 * Each SQL {@code DAO} receives a {@code Connection} from the {@code SqlConnectionManager}.
 *
 * @author Tanner Davis
 * @version 0.1
 * @see DAO
 * @see SqlConnectionManager
 * @since v0.1
 */

public class PersonSqlDAO implements DAO<Person> {
    private Connection connection = null;

    /**
     * Creates a row in the Person table of the database.
     *
     * @param person A {@code Person} model that will be used to get values
     * @return The {@code Person} model that came from the database
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public Person create(Person person) throws DatabaseException {
        return null;
    }

    /**
     * Updates a row in the Person table of the database.
     *
     * @param id The id of the row that needs to be updated
     * @param person A {@code Person} model that will be used to get values
     * @return The new {@code Person} model that came from the database
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public Person update(String id, Person person) throws DatabaseException {
        return null;
    }

    /**
     * Deletes a row in the Person table of the database.
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
     * Finds a row in the Person table by id.
     *
     * @param id The id of the row to find
     * @return The {@code Person} model representation that was found
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public Person findById(String id) throws DatabaseException {
        return null;
    }

    /**
     * Finds all rows in the Person table
     *
     * @return A {@code List} of {@code Person} models
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public List<Person> all() throws DatabaseException {
        return null;
    }

}
