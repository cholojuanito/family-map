package rbdavis.familymap.net.http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rbdavis.shared.models.http.requests.EventsRequest;
import rbdavis.shared.models.http.requests.LoginRequest;
import rbdavis.shared.models.http.requests.PeopleRequest;
import rbdavis.shared.models.http.requests.RegisterRequest;
import rbdavis.shared.models.http.responses.EventsResponse;
import rbdavis.shared.models.http.responses.LoginOrRegisterResponse;
import rbdavis.shared.models.http.responses.PeopleResponse;

import static rbdavis.shared.utils.Constants.*;

import static org.junit.Assert.*;

public class ServerProxyTest {

    private final String token = "I_made_a_permanent_token_to_use";
    private final String badToken ="not_in_DB";
    private final String userName = "johnDoe";
    private final String password = "secret";
    private final String email = "john@doe.com";
    private final String firstName = "John";
    private final String lastName = "Doe";
    private final String gender = "m";

    private final String badUserName = "fake";
    private final String badPassword = "noExist";
    // 4 generations worth of people and events
    private final int numPeople = 31;
    private final int numEvents = 108;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRegister() {
        RegisterRequest request = new RegisterRequest(userName, password, email, firstName, lastName, gender);
        LoginOrRegisterResponse response = ServerProxy.getInstance().register(request);

        // Always Duplicate Registration
        if (response != null) {
            assertEquals(USER_ID_TAKEN, response.getMessage());
        }
        else {
            System.out.println("Error connecting to the server");
        }

    }

    @Test
    public void testLogin() {
        LoginRequest request = new LoginRequest(userName, password);
        LoginOrRegisterResponse response = ServerProxy.getInstance().login(request);

        if (response != null) {
            assertEquals(userName, response.getUserName());
            assertEquals(SUCCESS, response.getMessage());
        }
        else {
            System.out.println("Error connecting to the server");
            return;
        }

        // Login again
        LoginOrRegisterResponse respDuplicate = ServerProxy.getInstance().login(request);

        if (respDuplicate != null) {
            assertEquals(userName, respDuplicate.getUserName());
            assertEquals(SUCCESS, respDuplicate.getMessage());
            assertNotEquals(respDuplicate.getAuthToken(), response.getAuthToken());
        }
        else {
            System.out.println("Error connecting to the server");
        }

        // Bad Login
        LoginRequest badRequest = new LoginRequest(badUserName, badPassword);
        LoginOrRegisterResponse badResponse = ServerProxy.getInstance().login(badRequest);

        if (badResponse != null) {
            assertEquals(INCORRECT_USERNAME, badResponse.getMessage());
        }
        else {
            System.out.println("Error connecting to the server");
        }
    }

    @Test
    public void testGetAllPeople() {
        PeopleRequest request = new PeopleRequest(token);
        PeopleResponse response = ServerProxy.getInstance().getAllPeople(request);

        if (response != null) {
            assertEquals(numPeople, response.getData().size());
        }
        else {
            System.out.println("Error connecting to the server");
            return;
        }

        // Bad Auth Token
        PeopleRequest badRequest = new PeopleRequest(badToken);
        PeopleResponse badResponse = ServerProxy.getInstance().getAllPeople(badRequest);

        if (badResponse != null) {
            assertEquals(UNAUTHORIZED_REQ_ERR, badResponse.getMessage());
        }
        else {
            System.out.println("Error connecting to the server");
        }
    }

    @Test
    public void testGetAllEvents() {
        EventsRequest request = new EventsRequest(token);
        EventsResponse response = ServerProxy.getInstance().getAllEvents(request);

        if (response != null) {
            assertEquals(numEvents, response.getData().size());
        }
        else {
            System.out.println("Error connecting to the server");
            return;
        }

        // Bad Auth Token
        EventsRequest badRequest = new EventsRequest(badToken);
        EventsResponse badResponse = ServerProxy.getInstance().getAllEvents(badRequest);

        if (badResponse != null) {
            assertEquals(UNAUTHORIZED_REQ_ERR, badResponse.getMessage());
        }
        else {
            System.out.println("Error connecting to the server");
        }
    }
}