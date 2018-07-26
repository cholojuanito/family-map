package rbdavis.server.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rbdavis.shared.models.data.Person;
import rbdavis.shared.models.http.requests.LoginRequest;
import rbdavis.shared.models.http.requests.PeopleRequest;
import rbdavis.shared.models.http.requests.PersonRequest;
import rbdavis.shared.models.http.responses.LoginOrRegisterResponse;
import rbdavis.shared.models.http.responses.PeopleResponse;
import rbdavis.shared.models.http.responses.PersonResponse;

import static org.junit.Assert.*;

public class PersonServiceTest {
    private final String username = "cholo";
    private final String password = "pass";
    private String currToken;
    private Person p1 = new Person("4", "cholo", "Tanner", "Davis", "M", "6", "5", null);
    private Person p2 = new Person("1", "juls", "Julianne", "Capito", "F", "2", "3", null);



    private PersonService serviceUnderTest;

    @Before
    public void setUp() throws Exception {
        serviceUnderTest = new PersonService();

        LoadServiceTest loader = new LoadServiceTest();
        loader.setUp();
        loader.testLoad();

        LoginOrRegisterResponse response = new LoginService().login(new LoginRequest(username, password));
        currToken = response.getAuthToken();
    }

    @After
    public void tearDown() throws Exception {
        serviceUnderTest = null;
        currToken = null;
    }

    @Test
    public void findAllPeople() {
        PeopleRequest request = new PeopleRequest(currToken);
        PeopleResponse response = serviceUnderTest.findAllPeople(request);

        assertEquals(response.getData().size(), 3);
    }

    @Test
    public void findPerson() {
        PersonRequest requestP1 = new PersonRequest(p1.getId(), currToken);
        PersonResponse responseP1 = serviceUnderTest.findPerson(requestP1);
        PersonRequest requestP2 = new PersonRequest(p2.getId(), currToken);
        PersonResponse responseP2 = serviceUnderTest.findPerson(requestP2);


        assertEquals(responseP1.getData().getFirstName(), p1.getFirstName());
        assertEquals(responseP1.getData().getUserId(), p1.getUserId());

        // User2 does not belong to user1's family tree
        assertNull(responseP2.getData());

    }
}