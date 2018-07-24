package rbdavis.server.services;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.server.database.sql.dataaccess.EventSqlDAO;
import rbdavis.server.database.sql.dataaccess.PersonSqlDAO;
import rbdavis.server.database.sql.dataaccess.UserSqlDAO;
import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.Location;
import rbdavis.shared.models.data.Person;
import rbdavis.shared.models.data.User;
import rbdavis.shared.models.http.requests.FillRequest;
import rbdavis.shared.models.http.responses.Response;
import rbdavis.shared.utils.MockDataGenerator;

/**
 * The service that performs the fill action for the "/fill/[username]/{generations}" endpoint.
 * <p>
 * Each service receives a version of a Request model and returns
 * a version of a Response model. Using the Request it then interacts with
 * the corresponding {@code DAO}.
 *
 * @author Tanner Davis
 * @version 0.1
 * @see Response
 * @see FillRequest
 * @since v0.1
 */

public class FillService extends Service {
    private SqlDatabase db = null;
    private static MockDataGenerator gen;
    private int generationCap = 4;
    private String userName = null;
    private int numPeopleCreated = 0;
    private int numEventsCreated = 0;

    static {
        try {
            gen = new MockDataGenerator();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error: Unable to open files with mock data.");
        }
    }

    private ArrayList<Person> generatedPeople = new ArrayList<>();
    private Map<String, Set<Event>> generatedEvents = new HashMap<>();

    public FillService() {}

    public FillService(SqlDatabase db) {
        this.db = db;
    }

    /**
     * Populates the database with generated data for the specified user.
     * The required "username" must belong to a user already registered with the server.
     * If there is any data in the database already associated with the given username, it is deleted.
     * <p>
     * Interacts with each model's version of a {@code DAO}.
     *
     * @param request - A {@code LoadRequest} that has a username
     *                and the number of generations to fill.
     * @return A {@code Response} object that carries the message and status code
     */
    public Response fill(FillRequest request) {
        Response response = new Response();
        this.userName = request.getUserName();
        this.generationCap = request.getNumGenerations();

        try {
            boolean commit = false;
            if (db == null) {
                db = new SqlDatabase();
            }
            UserSqlDAO userDao = db.getUserDao();
            PersonSqlDAO personDao = db.getPersonDao();
            EventSqlDAO eventDao = db.getEventDao();

            // Verify the User exists already
            User userFromDB = userDao.findById(request.getUserName());
            if (userFromDB != null) {
                // Clear any data associated with the username
                personDao.deleteByUsername(userName);
                eventDao.deleteByUsername(userName);

                // Create family tree data
                Person rootPerson = new Person(gen.generateUUID(), userName, userFromDB.getFirstName(),
                                               userFromDB.getLastName(), userFromDB.getGender(), null, null, null);
                createFamilyTree(rootPerson);

                // Persist the generated data to the database
                persistGeneratedData(personDao, eventDao);

                commit = true;
                response.setMessage("Added " + numPeopleCreated + " people and " + numEventsCreated + " events to the database");
            }
            else {
                response.setMessage("Error: Username unrecognized");
            }

            db.endTransaction(commit);
        }
        catch (DAO.DatabaseException | NullPointerException e) {
            if (db != null) {
                try {
                    db.endTransaction(false);
                }
                catch (DAO.DatabaseException worthLessException) {
                    logger.severe("Issue closing db connection");
                }
            }
            logger.warning(e.getMessage());
            response.setMessage(e.getMessage());
        }
        return response;
    }

    private void persistGeneratedData(PersonSqlDAO personDao, EventSqlDAO eventDao) throws DAO.DatabaseException {

        for (Person p : generatedPeople) {
            personDao.create(p);
        }

        for (Map.Entry<String, Set<Event>> entry : generatedEvents.entrySet()) {
            Set<Event> events = entry.getValue();
            for (Event e : events) {
                eventDao.create(e);
            }
        }
    }

    private void createFamilyTree(Person rootPerson) throws NullPointerException {
        LocalDate sept1994 = LocalDate.of(1994, Month.SEPTEMBER, 5);
        // Base person's father's side
        Person baseFather = createPersonsTree(1, Gender.M, 1994, rootPerson.getLastName());
        // Base person's mother's side
        Person baseMother = createPersonsTree(1, Gender.F, 1994, gen.generateLastName());

        if (baseFather != null & baseMother != null) {
            createParentsMarriageEvent(baseFather.getId(), baseMother.getId());
            baseFather.setSpouseID(baseMother.getId());
            baseMother.setSpouseID(baseFather.getId());

            rootPerson.setFatherId(baseFather.getId());
            rootPerson.setMotherID(baseMother.getId());
        }

        addNewFamilyMember(rootPerson);
        createEventsForPerson(false, rootPerson.getId(), sept1994);
    }

    private Person createPersonsTree(int currGeneration, Gender gender, int childBirthYear, String lastName) throws NullPointerException {
        if (currGeneration > generationCap) {
            return null;
        }
        String id = gen.generateUUID();
        LocalDate birthDate = gen.generateBirthDate(childBirthYear);
        boolean isDead = currGeneration > 3;
        String firstName = null;
        switch (gender) {
            case M:
                firstName = gen.generateMaleName();
                break;
            case F:
                firstName = gen.generateFemaleName();
                break;
        }

        createEventsForPerson(isDead, id, birthDate);

        String fatherId = null;
        String motherId = null;
        Person father = createPersonsTree(currGeneration + 1, Gender.M, birthDate.getYear(), lastName);
        Person mother = createPersonsTree(currGeneration + 1, Gender.F, birthDate.getYear(), gen.generateLastName());
        if (father != null) {
            fatherId = father.getId();
        }
        if (mother != null) {
            motherId = mother.getId();
        }

        if (father != null && mother != null) {
            createParentsMarriageEvent(fatherId, motherId);
            father.setSpouseID(motherId);
            mother.setSpouseID(fatherId);
        }

        Person newFamilyMember = new Person(id, userName, firstName, lastName, gender, fatherId, motherId, null);
        addNewFamilyMember(newFamilyMember);

        return newFamilyMember;
    }

    private void createEventsForPerson(boolean isDead, String personId, LocalDate birthDate) {
        Location birthLocation = gen.generateLocation();
        LocalDate baptismDate = gen.generateBaptismDate(birthDate.getYear());
        Location baptismLocation = gen.generateLocation();
        LocalDate deathDate = isDead ? gen.generateDeathDate(birthDate.getYear()) : null;
        Location deathLocation = isDead ? gen.generateLocation() : null;

        Event birth = new Event(gen.generateUUID(), personId, userName, Event.EventType.BIRTH,
                                birthLocation.getLatitude(), birthLocation.getLongitude(), birthLocation.getCity(),
                                birthLocation.getCountry(), birthDate);
        Event baptism = new Event(gen.generateUUID(), personId, userName, Event.EventType.BAPTISM,
                                  baptismLocation.getLatitude(), baptismLocation.getLongitude(), baptismLocation.getCity(),
                                  baptismLocation.getCountry(), baptismDate);

        addToPersonsEvents(birth, personId);
        addToPersonsEvents(baptism, personId);

        if (deathDate != null && deathLocation != null) {
            Event death = new Event(gen.generateUUID(), personId, userName, Event.EventType.DEATH, deathLocation.getLatitude(),
                              deathLocation.getLongitude(), deathLocation.getCity(), deathLocation.getCountry(),
                              deathDate);

            addToPersonsEvents(death, personId);
        }

    }

    private void createParentsMarriageEvent(String fatherId, String motherId) {
        int fatherBirthYear = findPersonsBirthYear(fatherId);
        int motherBirthYear = findPersonsBirthYear(motherId);
        int avgBirthYear = (fatherBirthYear + motherBirthYear) / 2;
        LocalDate marriageDate = gen.generateMarriageDate(avgBirthYear);
        Location marriageLocation = gen.generateLocation();

        Event fatherMarriageEvent = new Event(gen.generateUUID(), fatherId, userName, Event.EventType.MARRIAGE,
                                         marriageLocation.getLatitude(), marriageLocation.getLongitude(),
                                         marriageLocation.getCity(), marriageLocation.getCountry(),
                                         marriageDate);
        Event motherMarriageEvent = new Event(fatherMarriageEvent);
        motherMarriageEvent.setId(gen.generateUUID());
        motherMarriageEvent.setPersonId(motherId);

        addToPersonsEvents(fatherMarriageEvent, fatherId);
        addToPersonsEvents(motherMarriageEvent, motherId);
    }

    private int findPersonsBirthYear(String personId) {
        Set<Event> mothersEvents = generatedEvents.get(personId);
        for (Event e : mothersEvents) {
            if (e.getType() == Event.EventType.BIRTH) {
                return e.getDateHappened().getYear();
            }
        }
        return 0;
    }

    private void addNewFamilyMember(Person person) {
        generatedPeople.add(person);
        numPeopleCreated++;
    }

    private void addToPersonsEvents(Event event, String personId) {
        Set<Event> events = generatedEvents.get(personId);
        if (events == null) {
            events = new HashSet<>();
            events.add(event);
            generatedEvents.put(personId, events);
        }
        else {
            events.add(event);
        }
        numEventsCreated++;
    }
}
