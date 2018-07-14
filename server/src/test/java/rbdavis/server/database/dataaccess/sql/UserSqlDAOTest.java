package rbdavis.server.database.dataaccess.sql;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.dataaccess.UserSqlDAO;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.User;

public class UserSqlDAOTest {

    private UserSqlDAO daoUnderTest;

    @Before
    public void setUp() throws Exception
    {
        daoUnderTest = new UserSqlDAO();
    }

    @After
    public void tearDown() throws Exception
    {
        daoUnderTest = null;
    }

    @Test
    public void testCreate()
    {
        final String newUN = "myUsername";
        final String newID = UUID.randomUUID().toString();
        final String newPass = "secret_password";
        final String newEmail = "juls2011@hotmail.com";
        final String newFirst = "Julianne";
        final String newLast = "Capito";
        final Gender newGender = Gender.F;

        User user = new User(newUN, newID, newPass, newEmail, newFirst, newLast, newGender);
        try
        {
            daoUnderTest.create(user);
        }
        catch (DAO.DatabaseException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testUpdate()
    {
    }

    @Test
    public void testDelete()
    {
    }

    @Test
    public void testFindById()
    {
    }

    @Test
    public void testAll()
    {
    }
}