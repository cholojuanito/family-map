package rbdavis.server.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rbdavis.shared.models.data.AuthToken;
import rbdavis.shared.models.data.User;
import rbdavis.shared.models.http.requests.LoginRequest;
import rbdavis.shared.models.http.responses.LoginOrRegisterResponse;
import rbdavis.shared.models.http.responses.Response;

import static org.junit.Assert.*;

public class LoginServiceTest {

    @Before
    public void setUp() throws Exception {

        LoadServiceTest loader = new LoadServiceTest();
        loader.setUp();
        loader.testLoad();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testLogin() {
        User u1 = new User("juls", "1", "secret", "email@me",
                    "Julianne", "Capito", "F");
        User u2 = new User("cholo", "4", "pass", "email@me",
                   "Tanner", "Davis", "M");

        LoginRequest loginU1Req = new LoginRequest(u1.getUsername(), u1.getPassword());
        LoginRequest loginU2Req = new LoginRequest(u2.getUsername(), u2.getPassword());

        LoginOrRegisterResponse respU1 = new LoginService().login(loginU1Req);
        LoginOrRegisterResponse respU2 = new LoginService().login(loginU2Req);

        LoginOrRegisterResponse respU1Second = new LoginService().login(loginU1Req);
        LoginOrRegisterResponse respU2Second = new LoginService().login(loginU2Req);

        String authForU1 = respU1.getAuthToken();
        String authForU2 = respU2.getAuthToken();
        String authForU1Second = respU1Second.getAuthToken();
        String authForU2Second = respU2Second.getAuthToken();

        assertNotEquals(authForU1, authForU1Second);
        assertNotEquals(authForU2, authForU2Second);
        assertEquals(respU1.getUserName(), u1.getUsername());
        assertEquals(respU2.getUserName(), u2.getUsername());


    }
}