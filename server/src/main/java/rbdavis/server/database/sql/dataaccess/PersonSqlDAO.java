package rbdavis.server.database.sql.dataaccess;

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
    private static Logger logger;

    static {
        logger = Logger.getLogger("database");
    }

    private Connection connection;

    public PersonSqlDAO(Connection connection) {
        this.connection = connection;
    }

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
            PreparedStatement stmt = null;
            try {
                String sql = "INSERT INTO Persons " +
                        "(id, user_id, first_name, last_name, gender, father_id, mother_id, spouse_id)" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, person.getId());
                stmt.setString(2, person.getUserId());
                stmt.setString(3, person.getFirstName());
                stmt.setString(4, person.getLastName());
                stmt.setString(5, person.getGender().toString());
                stmt.setString(6, person.getFatherId());
                stmt.setString(7, person.getMotherId());
                stmt.setString(8, person.getSpouseId());
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
                    errorMessage = "Invalid SQL syntax.";
                    break;
                case 19:
                    errorMessage = "ID is already taken.";
                    break;
                default:
                    logger.severe("Person: create person failed " + e.getMessage());
                    errorMessage = "Create person failed.";
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
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
        try {
            PreparedStatement stmt = null;
            try {

                String sql = "UPDATE Persons " +
                             "SET first_name = ?, last_name = ?, gender = ?, father_id = ?," +
                             "mother_id = ?, spouse_id = ? " +
                             "WHERE id = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, person.getFirstName());
                stmt.setString(2, person.getLastName());
                stmt.setString(3, person.getGender().toString());
                stmt.setString(4, person.getFatherId());
                stmt.setString(5, person.getMotherId());
                stmt.setString(6, person.getSpouseId());
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
                    errorMessage = "Invalid SQL syntax.";
                    break;
                case 19:
                    errorMessage = "Couldn't find the row.";
                    break;
                default:
                    logger.severe("Person: update person failed " + e.getMessage());
                    errorMessage = "Update person failed.";
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
        return person;
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
        boolean worked = false;
        try {
            PreparedStatement stmt = null;
            try {

                String sql = "DELETE FROM Persons WHERE id = ?";
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
                    errorMessage = "Invalid SQL syntax.";
                    break;
                case 19:
                    errorMessage = "ID '" + id +"' does not exist";
                    break;
                default:
                    logger.severe("Person: delete person failed " + e.getMessage());
                    errorMessage = "Delete person failed.";
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
        return worked;
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
        Person foundPerson;
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {

                String sql = "SELECT * FROM Persons WHERE id = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, id);

                rs = stmt.executeQuery();
                if (!rs.next()) {
                    return null;
                }
                foundPerson = extractPersonModel(rs);

                return foundPerson;
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
                    errorMessage = "Invalid SQL syntax.";
                    break;
                case 19:
                    errorMessage = "ID '" + id + "' does not exist";
                    break;
                default:
                    logger.severe("Person: find person by id failed " + e.getMessage());
                    errorMessage = "find person by id failed.";
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
    }

    /**
     * Finds all rows in the Person table
     *
     * @return A {@code List} of {@code Person} models
     * @throws DatabaseException Any issue with the database is thrown
     */
    @Override
    public List<Person> all() throws DatabaseException {
        List<Person> foundPersons;
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {

                String sql = "SELECT * FROM Persons";
                stmt = connection.prepareStatement(sql);

                rs = stmt.executeQuery();
                foundPersons = extractPersonModels(rs);
                return foundPersons;
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
                    errorMessage = "Invalid SQL syntax.";
                    break;
                default:
                    logger.severe("Person: find all persons failed " + e.getMessage());
                    errorMessage = "Find all persons failed.";
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
    }

    /**
     * Deletes the whole Persons table
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

                String sql = "DELETE FROM Persons";
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
                    errorMessage = "Invalid SQL syntax.";
                    break;
                default:
                    logger.severe("Person: delete all persons failed " + e.getMessage());
                    errorMessage = "Delete all persons failed.";
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
        return worked;
    }

    /**
     * Uses the username to find all {@code Person}s associated with that user.
     *
     * @param username the foreign key used for finding {@code Person}s
     * @return A {@code List} of {@code Person} models
     */
    public List<Person> findByUsername(String username) throws DatabaseException {
        List<Person> foundPersons;
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {

                String sql = "SELECT * FROM Persons WHERE user_id = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, username);

                rs = stmt.executeQuery();
                foundPersons = extractPersonModels(rs);

                return foundPersons;
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
                    errorMessage = "Invalid SQL syntax.";
                    break;
                case 19:
                    errorMessage = "User id '" + username + "' does not exist";
                    break;
                default:
                    logger.severe("Person: find person by userId failed " + e.getMessage());
                    errorMessage = "Find person by userId failed.";
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
    }

    /**
     * Uses the username to delete all {@code Person}s associated with that user.
     *
     * @param username the foreign key used for deleting
     * @return True if anything was deleted. False otherwise.
     */
    public boolean deleteByUsername(String username) throws DatabaseException {
        boolean worked = false;
        try {
            PreparedStatement stmt = null;
            try {
                String sql = "DELETE FROM Persons WHERE user_id = ?";
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
                    errorMessage = "Invalid SQL syntax.";
                    break;
                case 19:
                    errorMessage = "User id '" + username + "' does not exist";
                    break;
                default:
                    logger.severe("Person: delete person by userId failed " + e.getMessage());
                    errorMessage = "Delete person by userId failed.";
                    break;
            }
            throw new DAO.DatabaseException(errorMessage);
        }
        return worked;
    }

    private Person extractPersonModel(ResultSet rs) throws SQLException {
        Person person;

        String id = rs.getString(1);
        String userId = rs.getString(2);
        String firstName = rs.getString(3);
        String lastName = rs.getString(4);
        String genderStr = rs.getString(5);
        String fatherId = rs.getString(6);
        String motherId = rs.getString(7);
        String spouseId = rs.getString(8);
        Gender gender;
        if (genderStr.equals("m")) {
            gender = Gender.M;
        }
        else {
            gender = Gender.F;
        }

        person = new Person(id, userId, firstName, lastName, gender, fatherId, motherId, spouseId);

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
