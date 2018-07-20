package rbdavis.server.database.sql.dataaccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlConnectionManager;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.Person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PersonSqlDAOTest {
    private Connection connection;
    final boolean doNotCommit = false;
    //Husband and wife
    private final String husbandID = "8e231070-d4a4-404e-b50b-30135c935a8a"; //UUID.randomUUID().toString();
    private final String wifeID = "8e2c071-d5p4-414e-b50n-y9735c935b8a";
    // Husband parents
    private final String fatherID1  = "632d54e3-3bf5-4f1f-b82e-93cf1b855217";
    private final String motherID1  = "fa49178c-e180-4ff5-9376-7ca08b3a89c8";
    // Wife parents
    private final String fatherID2  = "907efd25-07ce-4c33-8020-420a4beab2ea";
    private final String motherID2  = "d9fb6d00-9a0a-4030-9bf4-70815e504e40";
    // Husband g-pa's
    private final String grandpaID1 = "eeb64aa0-fd7b-40e9-b54a-bff861f72d20";
    private final String grandmaID1 = "be35e7e0-a429-4fcd-bc0b-3420f59ccf59";
    private final String grandpaID2 = "c548865e-73e0-421d-9dd8-ae5d7105e6fa";
    private final String grandmaID2 = "418a4aa6-e4ae-409a-ae77-da836a210c5f";
    // Wife g-pa's
    private final String grandpaID3 = "ce3d2352-eeff-4aa7-964d-98da921796df";
    private final String grandmaID3 = "567701c5-be42-400d-b680-4e39899fed1a";
    private final String grandpaID4 = "9d58b865-6b76-4f37-94a2-65b1f43be18c";
    private final String grandmaID4 = "692f7d8f-2a88-4363-b171-3d955e544425";
    // Random guy's parents
    private final String rgDadID = "65b823ed-3989-474e-8cb3-ad33df79ad93";
    private final String rgMomID = "e3d41d59-fb28-4758-b8bb-44eadc813218";

    private final String username = "myUsername";
    private final String username2 = "notSameUsername";

    private final Person husbandDad = new Person(fatherID1, username, "Rodney", "Davis", Gender.M, grandpaID1,
                                                 grandmaID1, motherID1);
    private final Person husbandMom = new Person(motherID1, username, "Barbara", "Henderson", Gender.F, grandpaID2,
                                                 grandmaID2, fatherID1);
    private final Person wifeDad = new Person(fatherID2, username, "Michael", "Capito", Gender.M, grandpaID3,
                                              grandmaID3, motherID2);
    private final Person wifeMom = new Person(motherID2, username, "Sharon", "Halsebo", Gender.F, grandpaID4,
                                              grandmaID4, fatherID2);
    private final Person randomGuy = new Person("randomId", username2, "John", "Doe", Gender.M, rgDadID, rgMomID, null);

    private PersonSqlDAO daoUnderTest;
    private Person husband, wife;
    @Before
    public void setUp() throws Exception {
        try {
            connection = SqlConnectionManager.openUnitTestConnection();
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new DAO.DatabaseException("Test database connection failed");
        }
        // Husband
        final String first = "Tanner";
        final String last = "Davis";
        final Gender gender = Gender.M;
        husband = new Person(husbandID, username, first, last, gender, fatherID1, motherID1, wifeID);
        // Wife
        final String newFirst = "Julianne";
        final String newLast = "Capito";
        final Gender newGender = Gender.F;
        wife = new Person(wifeID, username, newFirst, newLast, newGender, fatherID2, motherID2, husbandID);
        daoUnderTest = new PersonSqlDAO(connection);
    }

    @After
    public void tearDown() throws Exception {
        daoUnderTest = null;
        SqlConnectionManager.closeConnection(connection, doNotCommit);
    }

    @Test
    public void testCreateAndFind() {
        try {
            daoUnderTest.create(husband);
            daoUnderTest.create(wife);
            Person foundHusband = daoUnderTest.findById(husbandID);
            Person foundWife = daoUnderTest.findById(wifeID);


            assertNotNull(foundHusband);
            assertNotNull(foundWife);
            assertEquals(husband.getMotherID(), foundHusband.getMotherID());
            assertEquals(husband.getFatherId(), foundHusband.getFatherId());
            assertEquals(wife.getMotherID(), foundWife.getMotherID());
            assertEquals(wife.getFatherId(), foundWife.getFatherId());

            assertEquals(wifeID, foundHusband.getSpouseID());
            assertEquals(husbandID, foundWife.getSpouseID());
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testUpdate() {

        try {
            daoUnderTest.create(husband);
            daoUnderTest.create(wife);
            daoUnderTest.create(husbandDad);
            daoUnderTest.create(husbandMom);
            daoUnderTest.create(wifeDad);
            daoUnderTest.create(wifeMom);

            daoUnderTest.update(fatherID1, wifeDad);
            daoUnderTest.update(fatherID2, husbandDad);

            Person foundNewHusbandDad = daoUnderTest.findById(fatherID1);
            Person foundNewWifeDad = daoUnderTest.findById(fatherID2);

            assertNotNull(foundNewHusbandDad);
            assertNotNull(foundNewWifeDad);
            assertEquals(motherID2, foundNewHusbandDad.getSpouseID());
            assertEquals(motherID1, foundNewWifeDad.getSpouseID());

        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testDelete() {
        try {
            daoUnderTest.create(husband);
            daoUnderTest.create(wife);
            assertTrue(daoUnderTest.delete(husbandID));
            assertTrue(daoUnderTest.delete(wifeID));

            Person notFoundPerson = daoUnderTest.findById(husbandID);
            assertNull(notFoundPerson);
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testAll() {
        List<Person> people = null;
        try {
            daoUnderTest.create(husband);
            daoUnderTest.create(wife);
            daoUnderTest.create(husbandDad);
            daoUnderTest.create(husbandMom);
            daoUnderTest.create(wifeDad);
            daoUnderTest.create(wifeMom);

            people = daoUnderTest.all();
            assertTrue(people.size() == 6);

            daoUnderTest.delete(husbandID);

            people = daoUnderTest.all();
            assertTrue(people.size() == 5);
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testFindByUsername() {
        List<Person> people;
        try {
            daoUnderTest.create(husband);
            daoUnderTest.create(wife);
            daoUnderTest.create(husbandDad);
            daoUnderTest.create(husbandMom);
            daoUnderTest.create(wifeDad);
            daoUnderTest.create(wifeMom);
            // Has different username
            daoUnderTest.create(randomGuy);

            people = daoUnderTest.findByUsername(username);
            assertTrue(people.size() == 6);

            people = daoUnderTest.findByUsername(username2);
            assertTrue(people.size() == 1);

        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testDeleteByUsername() {
        List<Person> people;
        try {
            daoUnderTest.create(husband);
            daoUnderTest.create(wife);
            daoUnderTest.create(husbandDad);
            daoUnderTest.create(husbandMom);
            daoUnderTest.create(wifeDad);
            daoUnderTest.create(wifeMom);
            // Has different username
            daoUnderTest.create(randomGuy);

            assertTrue(daoUnderTest.deleteByUsername(username));
            people = daoUnderTest.findByUsername(username);
            assertTrue(people.size() == 0);

            people = daoUnderTest.findByUsername(username2);
            assertTrue(people.size() == 1);
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }
}