package rbdavis.server.database.sql.dataaccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import rbdavis.server.database.DAO;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.Person;
import rbdavis.shared.models.data.User;

import static org.junit.Assert.*;

public class PersonSqlDAOTest {
    //Husband and wife
    private final String personID1  = "8e231070-d4a4-404e-b50b-30135c935a8a"; //UUID.randomUUID().toString();
    private final String personID2  = "8e2c071-d5p4-414e-b50n-y9735c935b8a";
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
    // Husband great g-pa's
    private final String grandpa2ID1 = "a8fc23fb-4df0-4283-b7dd-ce923430edc9";
    private final String grandma2ID1 = "bf1d23d5-048c-4b68-836c-bc03db242b9b";
    private final String grandpa2ID2 = "65b823ed-3989-474e-8cb3-ad33df79ad93";
    private final String grandma2ID2 = "e3d41d59-fb28-4758-b8bb-44eadc813218";
    private final String grandpa2ID3 = "75217e16-6743-4c83-ba03-77fb176b20c9";
    private final String grandma2ID3 = "831d6c21-afe5-4917-8b2f-ea0893119f9b";
    private final String grandpa2ID4 = "1e9089de-b88f-4421-9c05-77a167464386";
    private final String grandma2ID4 = "b35900bf-dae5-45c2-ad8e-7439827e80b8";

    private final String username1 = "myUsername";
    private final String username2 = "cholo";

    private PersonSqlDAO daoUnderTest;
    private User husbandForTesting;
    @Before
    public void setUp() throws Exception {
        final String pass = "secret";
        final String email = "tanmanryan@gmail.com";
        final String first = "Tanner";
        final String last = "Davis";
        final Gender gender = Gender.M;

        husbandForTesting = new User(username1, personID1, pass, email, first, last, gender);
        //spouseForTesting = new User();
        daoUnderTest = new PersonSqlDAO();
    }

    @After
    public void tearDown() throws Exception {
        daoUnderTest = null;
        husbandForTesting = null;
    }

    @Test
    public void testCreate() {
        Person husband = new Person(husbandForTesting.getPersonId(), husbandForTesting.getUsername(), husbandForTesting.getFirstName(),
                                    husbandForTesting.getLastName(), husbandForTesting.getGender(), fatherID1, motherID1, personID2);
        Person father = new Person(fatherID1, husbandForTesting.getUsername(), "Rodney", "Davis", Gender.M, grandpaID1,
                                   grandmaID1, motherID1);
        Person mother = new Person(motherID1, husbandForTesting.getUsername(), "Barbara", "Davis", Gender.F, grandpaID2,
                                   grandmaID2, fatherID1);
        Person wife = new Person(personID2, husbandForTesting.getUsername(), "Julianne","Capito", Gender.F, fatherID2,
                                 motherID2, husbandForTesting.getPersonId());
        Person randomGuy = new Person(grandpaID4, "no one has this UN", "John", "Doe", Gender.M,
                                      "random'sDad", "random'sMom", null);
        try {
            daoUnderTest.create(husband);
            daoUnderTest.create(wife);
            daoUnderTest.create(father);
            daoUnderTest.create(mother);
            daoUnderTest.create(randomGuy);
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testUpdate() {
        Person newFather = new Person(fatherID1, husbandForTesting.getUsername(), "Michael", "Capito", Gender.M, grandpaID3,
                                      grandmaID3, motherID1);
        Person newMother = new Person(motherID1, husbandForTesting.getUsername(), "Sharon", "Halsebo", Gender.F, grandpaID4,
                                      grandmaID4, fatherID1);

        try {
            daoUnderTest.update(fatherID1, newFather);
            daoUnderTest.update(motherID1, newMother);
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testDelete() {
        try {
            assertTrue(daoUnderTest.delete(personID1));
            assertTrue(daoUnderTest.delete(personID2));
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testFindById() {
        Person foundPerson = null;
        try {
            foundPerson = daoUnderTest.findById(personID1);
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }

        assertNotNull(foundPerson);
        assertTrue(foundPerson.getId().equals(personID1));
    }

    @Test
    public void testAll() {
        List<Person> people = null;
        try {
            people = daoUnderTest.all();
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }

        assertNotNull(people);
        assertTrue(people.size() == 5);
    }

    @Test
    public void testFindByUsername() {
        List<Person> people = null;
        try {
            people = daoUnderTest.findByUsername(username1);
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }

        assertNotNull(people);
        assertTrue(people.size() == 4);
    }

    @Test
    public void testDeleteByUsername() {
        try {
            assertTrue(daoUnderTest.deleteByUsername(username1));
        }
        catch (DAO.DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }
}