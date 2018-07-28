package rbdavis.server.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rbdavis.shared.models.http.requests.PeopleRequest;
import rbdavis.shared.models.http.requests.PersonRequest;
import rbdavis.shared.models.http.requests.RegisterRequest;
import rbdavis.shared.models.http.responses.LoginOrRegisterResponse;
import rbdavis.shared.models.http.responses.PeopleResponse;
import rbdavis.shared.models.http.responses.PersonResponse;

import static org.junit.Assert.assertEquals;

public class RegisterServiceTest {
    private String username = "newUser";
    private String pasword = "newGuy";
    private String email = "thisSucks@gmail.com";
    private String firstName = "New guy";
    private String lastName = "On the block";
    private String gender = "M";

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
    public void testRegister() {
        RegisterRequest request = new RegisterRequest(username, pasword, email, firstName, lastName, gender);

        LoginOrRegisterResponse response = new RegisterService().register(request);

        PersonResponse personResponse = new PersonService().findPerson(new PersonRequest(response.getPersonID(), response.getAuthToken()));
        PeopleResponse peopleResponse = new PersonService().findAllPeople(new PeopleRequest(response.getAuthToken()));


        assertEquals(response.getUserName(), username);
        assertEquals(personResponse.getData().getUserId(), username);
        assertEquals(personResponse.getData().getFirstName(), firstName);

        assertEquals(peopleResponse.getData().size(), 31);
    }
}