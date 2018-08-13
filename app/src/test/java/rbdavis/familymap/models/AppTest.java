package rbdavis.familymap.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.Person;
import rbdavis.shared.utils.Constants;

import static org.junit.Assert.*;

public class AppTest {

    private App model = App.getInstance();
    private String username = "tanman";
    private Person user         = new Person("0", username, "Tanner", "Davis", Gender.M, "3", "2", "1");
    private Person wife         = new Person("1", username, "Julianne", "Capito", Gender.F, null, null, "0");
    private Person mom          = new Person("2", username, "Barbara", "Henderson", Gender.F, "7", "6", "3");
    private Person dad          = new Person("3", username, "Rod", "Davis", Gender.M, "5", "4", "2");
    private Person gMaFather    = new Person("4", username, "Caralee", "Hugentobler", Gender.F, null, null, "5");
    private Person gPaFather    = new Person("5", username, "Monte", "Davis", Gender.M, null, null, "4");
    private Person gMaMother    = new Person("6", username, "Rose Marie", "Steen-Nielsen", Gender.F, null, null, "7");
    private Person gPaMother    = new Person("7", username, "Douglas", "Henderson", Gender.M, null, null, "6");

    private Event userBirth             = new Event("0", user.getId(), username, "Birth", "0.00", "0.00", "American Fork", "USA",
                                            LocalDate.of(1994, Month.SEPTEMBER, 5));
    private Event wifeBirth             = new Event("1", wife.getId(), username, "Birth", "0.00", "0.00", "Oakland", "USA",
                                            LocalDate.of(1993, Month.FEBRUARY, 17));
    private Event fatherBirth           = new Event("2", dad.getId(), username, "Birth", "0.00", "0.00", "Twin Falls", "USA",
                                            LocalDate.of(1961, Month.FEBRUARY, 9));
    private Event motherBirth           = new Event("3", mom.getId(), username, "Birth", "0.00", "0.00", "Salt Lake City", "USA",
                                            LocalDate.of(1960, Month.APRIL, 11));
    private Event gMaMotherBirth        = new Event("4", gMaMother.getId(), username, "Birth", "0.00", "0.00", "Cape Town", "South Africa",
                                            LocalDate.of(1925, Month.JULY, 24));
    private Event gPaMotherBirth        = new Event("5", gPaMother.getId(), username, "Birth", "0.00", "0.00", "Calgary", "Canada",
                                            LocalDate.of(1930, Month.MARCH, 18));
    private Event gMaFatherBirth        = new Event("6", gMaFather.getId(), username, "Birth", "0.00", "0.00", "Taipei", "Taiwan",
                                            LocalDate.of(1933, Month.JANUARY, 5));
    private Event gPaFatherBirth        = new Event("7", gPaFather.getId(), username, "Birth", "0.00", "0.00", "Mos Eisley", "Tatooine",
                                            LocalDate.of(1935, Month.DECEMBER, 24));

    private Event userMarriage          = new Event("8", user.getId(), username, "Marriage", "0.00", "0.00", "Davis County", "USA",
                                            LocalDate.of(2017, Month.AUGUST, 5));
    private Event motherMarriage        = new Event("9", mom.getId(), username, "Marriage", "0.00", "0.00", "American Fork", "USA",
                                             LocalDate.of(1992, Month.AUGUST, 22));
    private Event gMaFatherMarriage     = new Event("10", gMaFather.getId(), username, "Marriage", "0.00", "0.00", "Somewhere", "Out there",
                                             LocalDate.of(1941, Month.APRIL, 10));
    private Event gPaFatherMarriage     = new Event("11", gPaFather.getId(), username, "Marriage", "0.00", "0.00", "American Fork", "USA",
                                                    LocalDate.of(1941, Month.APRIL, 10));

    @Before
    public void setUp() throws Exception {
        model.setUser(user);
        model.setUserPersonId(user.getId());

        model.setPeople(this.createPersonList());
        model.setEvents(this.createEventList());

        model.setPersonalEvents();
        model.setEventTypeColors();
        model.setFocusedPersonId(model.getUserPersonId());
        model.setPaternalAncestors();
        model.setMaternalAncestors();
        model.setSearchableList();
    }

    @After
    public void tearDown() throws Exception {
        model.resetModel(false);
    }

    private List<Person> createPersonList() {
        List<Person> people = new ArrayList<>();
        people.add(user);
        people.add(wife);
        people.add(mom);
        people.add(dad);
        people.add(gMaFather);
        people.add(gPaFather);
        people.add(gMaMother);
        people.add(gPaMother);

        return people;
    }

    private List<Event> createEventList() {
        List<Event> events = new ArrayList<>();
        events.add(userBirth);
        events.add(wifeBirth);
        events.add(motherBirth);
        events.add(fatherBirth);
        events.add(gMaMotherBirth);
        events.add(gPaMotherBirth);
        events.add(gMaFatherBirth);
        events.add(gPaFatherBirth);
        events.add(userMarriage);
        events.add(motherMarriage);
        events.add(gMaFatherMarriage);
        events.add(gPaFatherMarriage);

        return events;
    }

    @Test
    public void testLogout() {
        model.getSettings().setShowSpouseLines(false);
        model.getSettings().setShowAncestorsLines(false);

        assertFalse(model.getSettings().isShowSpouseLines());
        assertFalse(model.getSettings().isShowAncestorsLines());

        model.performLogout();

        assertEquals(0, model.getPeople().size());
        assertEquals(0, model.getEvents().size());

    }

    @Test
    public void testResetModel() {
        model.resetModel(false);

        assertEquals(0, model.getPeople().size());
        assertEquals(0, model.getEvents().size());
    }

    @Test
    public void testGetFilteredEventsByType() {
        // Get Birth only
        model.getFilters().getFilterOptions().put("Marriage", false);

        Map<String, Event> filteredEvents = model.getFilteredEvents();

        assertEquals(8, filteredEvents.size());

        // Get Marriage only
        model.getFilters().getFilterOptions().put("Marriage", true);
        model.getFilters().getFilterOptions().put("Birth", false);

        filteredEvents = model.getFilteredEvents();

        assertEquals(4, filteredEvents.size());

    }

    @Test
    public void testGetFilteredEventsByFatherSide() {
        model.getFilters().getFilterOptions().put(Constants.BY_FATHER_SIDE, false);
        Map<String, Event> filteredEvents = model.getFilteredEvents();

        assertEquals(6, filteredEvents.size());
    }

    @Test
    public void testGetFilteredEventsByMotherSide() {
        model.getFilters().getFilterOptions().put(Constants.BY_MOTHER_SIDE, false);
        Map<String, Event> filteredEvents = model.getFilteredEvents();

        assertEquals(7, filteredEvents.size());
    }

    @Test
    public void testGetFilteredEventsByMale() {
        model.getFilters().getFilterOptions().put(Constants.BY_MALE, false);
        Map<String, Event> filteredEvents = model.getFilteredEvents();

        assertEquals(6, filteredEvents.size());
    }

    @Test
    public void testGetFilteredEventsByFemale() {
        model.getFilters().getFilterOptions().put(Constants.BY_FEMALE, false);
        Map<String, Event> filteredEvents = model.getFilteredEvents();

        assertEquals(6, filteredEvents.size());
    }

    @Test
    public void testSearch() {
        List<SearchResult> results = model.search("ta");

        assertEquals(5, results.size());

        results = model.search("davis");

        assertEquals(8, results.size());

        results = model.search("south");

        assertEquals(1, results.size());

        results = model.search("");
        // Should find everything
        assertEquals(20, results.size());
    }
}