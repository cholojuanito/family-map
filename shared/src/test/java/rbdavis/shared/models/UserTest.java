package rbdavis.shared.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UserTest {

    private String username;
    private String personId;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;

    private User userUnderTest;

    @Before
    public void setUp() throws Exception {
        username = "rbdavis";
        personId = UUID.randomUUID().toString();
        password = "password";
        email = "tanmanryan@gmail.com";
        firstName = "Tanner";
        lastName = "Davis";
        gender = Gender.M;

        userUnderTest = new User(username, personId, password, email, firstName, lastName, gender);
    }

    @After
    public void tearDown() throws Exception {
        userUnderTest = null;
    }

    @Test
    public void testConstructor() {
        final String newUN = "juls2011";
        final String newID = UUID.randomUUID().toString();
        final String newPass = "secret_password";
        final String newEmail = "juls2011@hotmail.com";
        final String newFirst = "Julianne";
        final String newLast = "Capito";
        final Gender newGender = Gender.F;

        userUnderTest = new User(newUN, newID, newPass, newEmail, newFirst, newLast, newGender);

        assertEquals(newUN, userUnderTest.getUsername());
        assertNotEquals(this.personId, userUnderTest.getPersonId());
        assertEquals(newPass, userUnderTest.getPassword());
        assertEquals(newEmail, userUnderTest.getEmail());
        assertEquals(newFirst, userUnderTest.getFirstName());
        assertEquals(newLast, userUnderTest.getLastName());
        assertEquals(newGender.toString(), userUnderTest.getGender().toString());
    }

    @Test
    public void testSetters() {
        final String newUN = "juls2011";
        final String newPass = "secret_password";
        final String newEmail = "juls2011@hotmail.com";
        final String newFirst = "Julianne";
        final String newLast = "Capito";
        final Gender newGender = Gender.F;

        userUnderTest.setUsername(newUN);
        userUnderTest.setPassword(newPass);
        userUnderTest.setEmail(newEmail);
        userUnderTest.setFirstName(newFirst);
        userUnderTest.setLastName(newLast);
        userUnderTest.setGender(newGender);

        assertEquals(newUN, userUnderTest.getUsername());
        assertEquals(newPass, userUnderTest.getPassword());
        assertEquals(newEmail, userUnderTest.getEmail());
        assertEquals(newFirst, userUnderTest.getFirstName());
        assertEquals(newLast, userUnderTest.getLastName());
        assertEquals(newGender.toString(), userUnderTest.getGender().toString());
    }

    @Test
    public void testWithNullValues() {
        final String newUN = null;
        final String newPass = null;
        final String newEmail = null;
        final String newFirst = null;
        final String newLast = null;
        final Gender newGender = Gender.F;
    }
}