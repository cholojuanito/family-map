package rbdavis.server.database.sql.dataaccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlConnectionManager;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserSqlDAOTest {
    private Connection connection;
    final boolean doNotCommit = false;

    private UserSqlDAO daoUnderTest;
    private User userForTesting, user2ForTesting;
    private final String personID1 = "8e231070-d4a4-404e-b50b-30135c935a8a"; //UUID.randomUUID().toString();
    private final String username1 = "myUsername";
    private final String personID2 = "8e2c071-d5p4-414e-b50n-y9735c935b8a";
    private final String username2 = "cholo";

    @Before
    public void setUp() throws Exception {
        try {
            connection = SqlConnectionManager.openUnitTestConnection();
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new DAO.DatabaseException("Test database connection failed");
        }
        // User 1
        final String pass = "secret";
        final String email = "tanmanryan@gmail.com";
        final String first = "Tanner";
        final String last = "Davis";
        final Gender gender = Gender.M;
        userForTesting = new User(username1, personID1, pass, email, first, last, gender);
        // User 2
        final String newPass = "secret_password";
        final String newEmail = "juls2011@hotmail.com";
        final String newFirst = "Julianne";
        final String newLast = "Capito";
        final Gender newGender = Gender.F;
        user2ForTesting = new User(username2, personID2, newPass, newEmail, newFirst, newLast, newGender);

        daoUnderTest = new UserSqlDAO(connection);
    }

    @After
    public void tearDown() throws Exception {
        SqlConnectionManager.closeConnection(connection, doNotCommit);
        daoUnderTest = null;
    }

    @Test
    public void testCreateAndFindById() {
        try {
            daoUnderTest.create(userForTesting);
            daoUnderTest.create(user2ForTesting);
            User foundUser1 = daoUnderTest.findById(username1);
            User foundUser2 = daoUnderTest.findById(username2);

            assertNotNull(foundUser1);
            assertNotNull(foundUser2);
            assertEquals(userForTesting.getUsername(), foundUser1.getUsername());
            assertEquals(userForTesting.getPersonId(), foundUser1.getPersonId());
            assertEquals(user2ForTesting.getUsername(), foundUser2.getUsername());
            assertEquals(user2ForTesting.getPersonId(), foundUser2.getPersonId());
        }
        catch (DAO.DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoubleCreate() {
        try {
            daoUnderTest.create(userForTesting);
            daoUnderTest.create(userForTesting);
        }
        catch (DAO.DatabaseException e) {
            assertEquals("Username is already taken", e.getMessage());
        }
    }

    @Test
    public void testUpdate() {
        try {
            daoUnderTest.create(userForTesting);
            daoUnderTest.create(user2ForTesting);

            daoUnderTest.update(username1, user2ForTesting);
            daoUnderTest.update(username2, userForTesting);

            User foundUser1 = daoUnderTest.findById(username1);
            User foundUser2 = daoUnderTest.findById(username2);

            assertNotNull(foundUser1);
            assertNotNull(foundUser2);
            assertEquals(userForTesting.getFirstName(), foundUser2.getFirstName());
            assertEquals(userForTesting.getLastName(), foundUser2.getLastName());
            assertEquals(user2ForTesting.getFirstName(), foundUser1.getFirstName());
            assertEquals(user2ForTesting.getLastName(), foundUser1.getLastName());
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testAll() {
        try {
            daoUnderTest.create(userForTesting);
            daoUnderTest.create(user2ForTesting);
            List<User> foundUsers = daoUnderTest.all();
            assertNotNull(foundUsers);
            assertTrue(foundUsers.size() == 2);
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testDelete() {
        try {
            daoUnderTest.create(userForTesting);
            daoUnderTest.create(user2ForTesting);
            assertTrue(daoUnderTest.delete(username1));
            assertTrue(daoUnderTest.delete(username2));

            User notFoundUser = daoUnderTest.findById(username1);
            assertNull(notFoundUser);
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }
}