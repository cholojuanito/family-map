package rbdavis.shared.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.junit.Assert.*;

public class EventTest {

    private String id;
    private String personId;
    private String userId;
    private Event.EventType type;
    private String latitude;
    private String longitude;
    private String city;
    private String country;
    private LocalDate dateHappened;

    Event eventUnderTest;

    @Before
    public void setUp() throws Exception
    {
        id = UUID.randomUUID().toString();
        personId = UUID.randomUUID().toString();
        userId = UUID.randomUUID().toString();
        type = Event.EventType.BIRTH;
        latitude = "52.379189";
        longitude = "4.899431";
        city = "Amsterdam";
        country = "Netherlands";
        dateHappened = LocalDate.of(1993, Month.DECEMBER, 25);

        eventUnderTest = new Event(id, personId, userId, type, latitude, longitude, city, country, dateHappened);
    }

    @After
    public void tearDown() throws Exception
    {
        eventUnderTest = null;
    }

    @Test
    public void testConstructor()
    {
        String newId = UUID.randomUUID().toString();
        String newPersonId = UUID.randomUUID().toString();
        String newUserId = UUID.randomUUID().toString();
        Event.EventType newType = Event.EventType.MARRIAGE;
        String newLat = "37.8044";
        String newLong = "122.2711";
        String newCity = "Oakland";
        String newCountry = "United States";
        LocalDate newHappenedOn = LocalDate.of(2017, Month.AUGUST, 5);

        eventUnderTest = new Event(newId, newPersonId, newUserId, newType, newLat, newLong, newCity, newCountry, newHappenedOn);

        assertNotEquals(this.id, eventUnderTest.getId());
        assertNotEquals(this.personId, eventUnderTest.getPersonId());
        assertNotEquals(this.userId, eventUnderTest.getUserId());
        assertEquals(newType, eventUnderTest.getType());
        assertEquals(newLat, eventUnderTest.getLatitude());
        assertEquals(newLong, eventUnderTest.getLongitude());
        assertEquals(newCity, eventUnderTest.getCity());
        assertEquals(newCountry, eventUnderTest.getCountry());
        assertEquals(newHappenedOn.toString(), eventUnderTest.getDateHappened().toString());
    }
}