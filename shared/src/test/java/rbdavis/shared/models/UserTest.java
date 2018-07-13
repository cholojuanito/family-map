package rbdavis.shared.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class UserTest
{

    private String username;
    private String personId;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;

    private User userUnderTest;

    @Before
    public void setUp() throws Exception
    {
        username = "rbdavis";
        personId = UUID.randomUUID().toString();
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testSetters()
    {
    }
}