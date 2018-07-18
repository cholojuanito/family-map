package rbdavis.server.database.sql.dataaccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.dataaccess.UserSqlDAO;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.User;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UserSqlDAOTest {

    private UserSqlDAO daoUnderTest;
    private User userForTesting;
    private final String personID1 = "8e231070-d4a4-404e-b50b-30135c935a8a"; //UUID.randomUUID().toString();
    private final String username1 = "myUsername";
    private final String personID2 = "8e2c071-d5p4-414e-b50n-y9735c935b8a";
    private final String username2 = "cholo";

    @Before
    public void setUp() throws Exception {
        final String pass = "secret";
        final String email = "tanmanryan@gmail.com";
        final String first = "Tanner";
        final String last = "Davis";
        final Gender gender = Gender.M;

        userForTesting = new User(username1, personID1, pass, email, first, last, gender);
        daoUnderTest = new UserSqlDAO();
    }

    @After
    public void tearDown() throws Exception {
        daoUnderTest = null;
    }

    @Test
    public void testCreate() {
        try {
            daoUnderTest.create(userForTesting);
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testUpdate() {
        final String newPass = "secret_password";
        final String newEmail = "juls2011@hotmail.com";
        final String newFirst = "Julianne";
        final String newLast = "Capito";
        final Gender newGender = Gender.F;

        userForTesting.setPassword(newPass);
        userForTesting.setEmail(newEmail);
        userForTesting.setFirstName(newFirst);
        userForTesting.setLastName(newLast);
        userForTesting.setGender(newGender);

        try {
            daoUnderTest.update(username1, userForTesting);
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testDelete() {
        try {
            assertTrue(daoUnderTest.delete(username1));
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testFindById() {
        //User expectedUser = new User();
        try {
            User foundUser = daoUnderTest.findById(username2);
            assertNotNull(foundUser);
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testAll() {
        try {
            List<User> foundUsers = daoUnderTest.all();
            assertNotNull(foundUsers);
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }
}