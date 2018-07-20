package rbdavis.server.database.sql.dataaccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlConnectionManager;
import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.Person;

import static org.junit.Assert.*;

public class EventSqlDAOTest {
    private Connection connection;
    final boolean doNotCommit = false;
    private final String husbandID = "8e231070-d4a4-404e-b50b-30135c935a8a"; //UUID.randomUUID().toString();
    private final String wifeID = "8e2c071-d5p4-414e-b50n-y9735c935b8a";
    private final String first = "Tanner";
    private final String last = "Davis";
    private final Gender gender = Gender.M;
    private final String newFirst = "Julianne";
    private final String newLast = "Capito";
    private final Gender newGender = Gender.F;
    private final String username = "cholo";

    private final Person husband = new Person(husbandID, username, first, last, gender, null, null, wifeID);
    private final Person wife = new Person(wifeID, username, newFirst, newLast, newGender, null, null, husbandID);

    private final String hBirthID = "eeb64aa0-fd7b-40e9-b54a-bff861f72d20";
    private final String wBirthID = "be35e7e0-a429-4fcd-bc0b-3420f59ccf59";
    private final String hBaptismID  = "632d54e3-3bf5-4f1f-b82e-93cf1b855217";
    private final String wBaptismID  = "fa49178c-e180-4ff5-9376-7ca08b3a89c8";
    private final String hMarriageID  = "907efd25-07ce-4c33-8020-420a4beab2ea";
    private final String wMarriageID = "d9fb6d00-9a0a-4030-9bf4-70815e504e40";

    private final DateTimeFormatter EVENT_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private final LocalDate hBirthday = LocalDate.of(1994, Month.SEPTEMBER, 5);
    private final LocalDate wBirthday = LocalDate.of(1993, Month.FEBRUARY, 17);
    private final LocalDate hBaptismDay = hBirthday.plusYears(8);
    private final LocalDate wBaptismDay = wBirthday.plusYears(8);
    private final LocalDate marriageDay = LocalDate.of(2017, Month.AUGUST, 5);


    private final Event husbandBirth = new Event(hBirthID, husbandID, username, Event.EventType.BIRTH, "79.9833", "-84.0667", "Eureka", "Canada", hBirthday);
    private final Event husbandBaptism = new Event(hBaptismID, husbandID, username, Event.EventType.BAPTISM, "79.9833", "-84.0667", "Eureka", "Canada", hBaptismDay);
    private final Event husbandMarriage = new Event(hMarriageID, husbandID, username, Event.EventType.MARRIAGE, "37.8044", "122.2711", "Oakland", "California", marriageDay);
    private final Event wifeBirth = new Event(wBirthID, wifeID, username, Event.EventType.BIRTH, "45.5", "-72.4333", "Montreal", "Canada", wBirthday);
    private final Event wifeBaptism = new Event(wBaptismID, wifeID, username, Event.EventType.BAPTISM, "45.5", "-72.4333", "Montreal", "Canada", wBaptismDay);
    private final Event wifeMarriage = new Event(wMarriageID, wifeID, username, Event.EventType.MARRIAGE, "37.8044", "122.2711", "Oakland", "California", marriageDay);

    private final Event differentUsername = new Event("randomId", "personId", "username", Event.EventType.BIRTH, "0.00", "0.00", "Greenwich", "England", LocalDate.now());

    private EventSqlDAO daoUnderTest;

    @Before
    public void setUp() throws Exception {
        try {
            connection = SqlConnectionManager.openUnitTestConnection();
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new DAO.DatabaseException("Test database connection failed");
        }
        daoUnderTest = new EventSqlDAO(connection);
    }

    @After
    public void tearDown() throws Exception {
        SqlConnectionManager.closeConnection(connection, doNotCommit);
        daoUnderTest = null;
    }

    @Test
    public void testCreateAndFind() {
        try {
            daoUnderTest.create(husbandBirth);
            daoUnderTest.create(wifeBirth);
            Event foundHBirth = daoUnderTest.findById(hBirthID);
            Event foundWBirth = daoUnderTest.findById(wBirthID);


            assertNotNull(foundHBirth);
            assertNotNull(foundWBirth);
            assertEquals(husbandID, foundHBirth.getPersonId());
            assertEquals(hBirthday.format(EVENT_FORMATTER), foundHBirth.getDateHappened().format(EVENT_FORMATTER));
            assertEquals(wifeID, foundWBirth.getPersonId());
            assertEquals(wBirthday.format(EVENT_FORMATTER), foundWBirth.getDateHappened().format(EVENT_FORMATTER));
        }
        catch (DAO.DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdate() {
        try {
            daoUnderTest.create(husbandBirth);
            daoUnderTest.create(wifeBirth);

            LocalDate newHBirth = LocalDate.of(1985, Month.JUNE, 29);
            LocalDate newWBirth = LocalDate.of(1990, Month.DECEMBER, 25);
            husbandBirth.setDateHappened(newHBirth);
            wifeBirth.setDateHappened(newWBirth);

            daoUnderTest.update(hBirthID, husbandBirth);
            daoUnderTest.update(wBirthID, wifeBirth);

            Event foundHBirth = daoUnderTest.findById(hBirthID);
            Event foundWBirth = daoUnderTest.findById(wBirthID);

            assertNotNull(foundHBirth);
            assertNotNull(foundWBirth);
            assertEquals(husbandID, foundHBirth.getPersonId());
            assertEquals(newHBirth.format(EVENT_FORMATTER), foundHBirth.getDateHappened().format(EVENT_FORMATTER));
            assertNotEquals(hBirthday.format(EVENT_FORMATTER), foundHBirth.getDateHappened().format(EVENT_FORMATTER));
            assertEquals(wifeID, foundWBirth.getPersonId());
            assertNotEquals(wBirthday.format(EVENT_FORMATTER), foundWBirth.getDateHappened().format(EVENT_FORMATTER));
            assertEquals(newWBirth.format(EVENT_FORMATTER), foundWBirth.getDateHappened().format(EVENT_FORMATTER));
        }
        catch (DAO.DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDelete() {
        try {
            daoUnderTest.create(husbandBirth);
            daoUnderTest.create(wifeBirth);

            assertTrue(daoUnderTest.delete(hBirthID));

            Event foundEvent = daoUnderTest.findById(hBirthID);

            assertNull(foundEvent);
        }
        catch (DAO.DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAll() {
        try {
            daoUnderTest.create(husbandBirth);
            daoUnderTest.create(wifeBirth);
            daoUnderTest.create(husbandBaptism);
            daoUnderTest.create(wifeBaptism);
            daoUnderTest.create(husbandMarriage);
            daoUnderTest.create(wifeMarriage);

            List<Event> foundEvents = daoUnderTest.all();

            assertTrue(foundEvents.size() == 6);

        }
        catch (DAO.DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindByUsername() {
        try {
            daoUnderTest.create(husbandBirth);
            daoUnderTest.create(wifeBirth);
            daoUnderTest.create(husbandBaptism);
            daoUnderTest.create(wifeBaptism);
            daoUnderTest.create(husbandMarriage);
            daoUnderTest.create(wifeMarriage);
            daoUnderTest.create(differentUsername);

            List<Event> foundEvents = daoUnderTest.findByUsername(username);
            assertNotNull(foundEvents);
            assertTrue(foundEvents.size() == 6);

            foundEvents = daoUnderTest.findByUsername("username");
            assertNotNull(foundEvents);
            assertTrue(foundEvents.size() == 1);

        }
        catch (DAO.DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteByUsername() {
        try {
            daoUnderTest.create(husbandBirth);
            daoUnderTest.create(wifeBirth);
            daoUnderTest.create(husbandBaptism);
            daoUnderTest.create(wifeBaptism);
            daoUnderTest.create(husbandMarriage);
            daoUnderTest.create(wifeMarriage);
            daoUnderTest.create(differentUsername);

            assertTrue(daoUnderTest.deleteByUsername(username));

            List<Event> foundEvents = daoUnderTest.all();
            assertTrue(foundEvents.size() == 1);

            assertFalse(daoUnderTest.deleteByUsername("uNThatDoesn'tExist"));
            Event notFoundEvent = daoUnderTest.findById(hBirthID);
            assertNull(notFoundEvent);

        }
        catch (DAO.DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindByPersonId() {
        try {
            daoUnderTest.create(husbandBirth);
            daoUnderTest.create(wifeBirth);
            daoUnderTest.create(husbandBaptism);
            daoUnderTest.create(wifeBaptism);
            daoUnderTest.create(husbandMarriage);
            daoUnderTest.create(wifeMarriage);
            daoUnderTest.create(differentUsername);

            List<Event> foundEvents = daoUnderTest.findByPersonId(husbandID);
            assertTrue(foundEvents.size() == 3);

            foundEvents = daoUnderTest.findByPersonId("idThatDoesn'tExist");
            assertTrue(foundEvents.size() == 0);

        }
        catch (DAO.DatabaseException e) {
            e.printStackTrace();
        }
    }
}