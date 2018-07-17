package rbdavis.server.database.sql.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlConnectionManager;
import rbdavis.shared.models.data.Gender;
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
        try {
            connection = SqlConnectionManager.openConnection();
            connection.setAutoCommit(false);
            PreparedStatement stmt = null;
            try {
                String sql = "INSERT INTO Persons" +
                        "(id, user_id, first_name, last_name, gender, father_id, mother_id, spouse_id)" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, person.getId());
                stmt.setString(2, person.getUserId());
                stmt.setString(3, person.getFirstName());
                stmt.setString(4, person.getLastName());
                stmt.setString(5, person.getGender().toString());
                stmt.setString(6, person.getFatherId());
                stmt.setString(7, person.getMotherID());
                stmt.setString(8, person.getSpouseID());

                if (stmt.executeUpdate() == 1) {
                    connection.commit();
                }
                else {
                    connection.rollback();
                }
            }
            finally {
                closeStatement(stmt);
                SqlConnectionManager.closeConnection(connection);
            }
        }
        catch (SQLException e) {
            String errorMessage;
            switch (e.getErrorCode()) {
                case 1:
                    errorMessage = "Invalid SQL syntax.";
                    break;
                case 19:
                    errorMessage = "ID is already taken.";
                    break;
                default:
                    errorMessage = "Create person failed.";
                    break;
            }
            throw new DAO.DatabaseException(errorMessage, e);
        }
        return person;
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

    /**
     * Uses the username to find all {@code Person}s associated with that user.
     *
     * @param username the foreign key used for finding {@code Person}s
     * @return A {@code List} of {@code Person} models
     */
    public List<Person> findByUsername(String username) {
        return null;
    }

    /**
     * Uses the username to delete all {@code Person}s associated with that user.
     *
     * @param username the foreign key used for deleting
     * @return True if anything was deleted. False otherwise.
     */
    public boolean deleteByUsername(String username) {
        return false;
    }

    private Person extractPersonModel(ResultSet rs) throws SQLException {
        Person person = null;

        String id = rs.getString(1);
        String userId = rs.getString(2);
        String firstName = rs.getString(3);
        String lastName = rs.getString(4);
        String genderStr = rs.getString(5);
        String fatherId = rs.getString(6);
        String motherId = rs.getString(7);
        String spouseid = rs.getString(8);
        Gender gender;
        if (genderStr.equals("m")) {
            gender = Gender.M;
        }
        else {
            gender = Gender.F;
        }

        person = new Person(id, userId, firstName, lastName, gender, fatherId, motherId, spouseid);

        return person;
    }

    private List<Person> extractPersonModels(ResultSet rs) throws SQLException {
        List<Person> people = new ArrayList<>();

        while (rs.next()) {
            Person person = extractPersonModel(rs);
            if (person != null) {
                people.add(person);
            }
        }

        return people;
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
