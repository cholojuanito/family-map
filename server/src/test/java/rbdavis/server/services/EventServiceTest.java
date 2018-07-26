package rbdavis.server.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.User;
import rbdavis.shared.models.http.requests.EventRequest;
import rbdavis.shared.models.http.requests.EventsRequest;
import rbdavis.shared.models.http.requests.LoginRequest;
import rbdavis.shared.models.http.responses.EventResponse;
import rbdavis.shared.models.http.responses.EventsResponse;
import rbdavis.shared.models.http.responses.LoginOrRegisterResponse;

import static org.junit.Assert.*;

public class EventServiceTest {

    private final String username = "cholo";
    private final String password = "pass";
    private String currToken;

    private final String hBirthID = "4";
    private final String wBirthID = "1";

    private final LocalDate hBirthday = LocalDate.of(1994, Month.SEPTEMBER, 5);
    private final LocalDate wBirthday = LocalDate.of(1993, Month.FEBRUARY, 17);


    private final Event husbandBirth = new Event("4", "4", "cholo", "BIRTH", "41.5833",
                                                 "-92.3833", "American Fork", "United States", hBirthday);

    private final Event wifeBirth = new Event("1", "1", "juls", "BIRTH", "41.5833",
                                              "-92.3833", "Des Moines", "United States", wBirthday);

    private EventService serviceUnderTest;

    @Before
    public void setUp() throws Exception {
        serviceUnderTest = new EventService();

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
    public void testFindAllEvents() {
        EventsRequest request = new EventsRequest(currToken);
        EventsResponse response = serviceUnderTest.findAllEvents(request);

        assertEquals(response.getData().size(), 3);
    }

    @Test
    public void testFindEvent() {
        EventRequest request1 = new EventRequest(hBirthID, currToken);
        EventResponse response1 = serviceUnderTest.findEvent(request1);

        EventRequest request2 = new EventRequest(wBirthID, currToken);
        EventResponse response2 = serviceUnderTest.findEvent(request2);

        assertEquals(response1.getData().getId(), husbandBirth.getId());
        assertEquals(response1.getData().getPersonId(), husbandBirth.getPersonId());
        assertEquals(response1.getData().getCity(), husbandBirth.getCity());

        assertNull(response2.getData());
    }
}