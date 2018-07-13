package rbdavis.server.database.dataaccess.sql;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.util.UUID;

import rbdavis.server.database.dataaccess.DAO;
import rbdavis.shared.models.Gender;
import rbdavis.shared.models.User;

import static org.junit.Assert.*;

public class SqlDAOTest
{
    SqlDAO sqlDAOUnderTest;

    @Before
    public void setUp() throws Exception
    {
        sqlDAOUnderTest = new SqlDAO();
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testCreateUser()
    {
        final String newUN = "juls2011";
        final String newID = UUID.randomUUID().toString();
        final String newPass = "secret_password";
        final String newEmail = "juls2011@hotmail.com";
        final String newFirst = "Julianne";
        final String newLast = "Capito";
        final Gender newGender = Gender.F;
        User user = new User(newUN, newID, newPass, newEmail, newFirst, newLast, newGender);

        try
        {
            sqlDAOUnderTest.createAUser(user);
        }
        catch (DAO.DatabaseException e)
        {
            System.out.println(e.getMessage());
        }
    }
}