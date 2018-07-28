package rbdavis.shared.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import rbdavis.shared.models.data.Location;

import static java.util.Calendar.APRIL;
import static java.util.Calendar.AUGUST;
import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.FEBRUARY;
import static java.util.Calendar.JANUARY;
import static java.util.Calendar.JUNE;
import static java.util.Calendar.MARCH;
import static java.util.Calendar.MAY;
import static java.util.Calendar.NOVEMBER;
import static java.util.Calendar.OCTOBER;
import static java.util.Calendar.SEPTEMBER;
import static rbdavis.shared.utils.Constants.F_NAMES_PATH;
import static rbdavis.shared.utils.Constants.LAST_NAMES_PATH;
import static rbdavis.shared.utils.Constants.LOCATIONS_PATH;
import static rbdavis.shared.utils.Constants.M_NAMES_PATH;

public class MockDataGenerator {
    // Constants
    private static final Type LIST_NAMES_TYPE = new TypeToken<ArrayList<String>>() {}.getType();
    private static final Type LIST_LOCATIONS_TYPE = new TypeToken<ArrayList<Location>>() {}.getType();
    private static final int FIRST = 1;
    private static final int TWENTY_EIGHTH = 28;
    private static final int THIRTIETH = 30;
    private static final int THIRTY_FIRST = 31;
    private static final int ACCOUNTABLE_AGE = 8;
    private static final int MIN_AGE_CHILDBEARING = 25;
    private static final int MAX_AGE_CHILDBEARING = 35;
    private static final int MARRIAGE_PRIME = 20;
    private static final int MARRIAGE_NOT_SO_PRIME = 35;
    private static final int MIN_AGE_DEATH = 75;
    private static final int MAX_AGE_DEATH = 85;

    private GsonBuilder gb = new GsonBuilder().serializeNulls();
    private Gson gson = gb.create();

    private ArrayList<String> maleFirstNames;
    private ArrayList<String> femaleFirstNames;
    private ArrayList<String> lastNames;
    private ArrayList<Location> locations;

    public MockDataGenerator() throws FileNotFoundException {
        readMaleFirstNames();
        readFemaleFirstNames();
        readLastNames();
        readLocations();
    }

    public String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public String generateMaleName() {
        return maleFirstNames.get(randomIndex(maleFirstNames.size()));
    }

    public String generateFemaleName() {
        return femaleFirstNames.get(randomIndex(femaleFirstNames.size()));
    }

    public String generateLastName() {
        return lastNames.get(randomIndex(lastNames.size()));
    }

    public Location generateLocation() {
        return locations.get(randomIndex(locations.size()));
    }

    public LocalDate generateBirthDate(int childBirthYear) {
        return createRandomDate(childBirthYear - MAX_AGE_CHILDBEARING, childBirthYear - MIN_AGE_CHILDBEARING);
    }

    public LocalDate generateBaptismDate(int birthYear) {
        return createRandomDate(birthYear + ACCOUNTABLE_AGE);
    }

    public LocalDate generateMarriageDate(int avgBirthYear) {
        return createRandomDate(avgBirthYear + MARRIAGE_PRIME, avgBirthYear + MARRIAGE_NOT_SO_PRIME);
    }

    public LocalDate generateDeathDate(int birthYear) {
        return createRandomDate(birthYear + MIN_AGE_DEATH, birthYear + MAX_AGE_DEATH);
    }

    public static LocalDate createRandomDate(int year) {
        int month = randomIntBetween(JANUARY, DECEMBER);
        int day = createRandomDayBasedOnMonth(month);
        month = month + 1;
        return LocalDate.of(year, month, day);
    }

    public static LocalDate createRandomDate(int startYear, int endYear) {
        int month = randomIntBetween(JANUARY, DECEMBER);
        int day = createRandomDayBasedOnMonth(month);
        int year = randomIntBetween(startYear, endYear);
        // Need to add 1 as an offset between the Calendar and LocalDate classes
        month = month + 1;
        return LocalDate.of(year, month, day);
    }

    private static int createRandomDayBasedOnMonth(int month) {
        switch (month) {
            case APRIL:
            case JUNE:
            case SEPTEMBER:
            case NOVEMBER:
                return randomIntBetween(FIRST, THIRTIETH);
            case JANUARY:
            case MARCH:
            case MAY:
            case AUGUST:
            case OCTOBER:
            case DECEMBER:
                return randomIntBetween(FIRST, THIRTY_FIRST);
            case FEBRUARY:
            default:
                return randomIntBetween(FIRST, TWENTY_EIGHTH);
        }
    }

    private static int randomIntBetween(int start, int end) {
        Random random = new Random();
        return random.nextInt(end + 1 - start) + start;
    }

    private static int randomIndex(int arrayLength) {
        Random random = new Random();
        return random.nextInt(arrayLength);
    }

    private void readMaleFirstNames() throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader(M_NAMES_PATH));
        ArrayList<String> maleNames = gson.fromJson(reader, LIST_NAMES_TYPE);
        setMaleFirstNames(maleNames);
    }

    private void readFemaleFirstNames() throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader(F_NAMES_PATH));
        ArrayList<String> femaleNames = gson.fromJson(reader, LIST_NAMES_TYPE);
        setFemaleFirstNames(femaleNames);
    }

    private void readLastNames() throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader(LAST_NAMES_PATH));
        ArrayList<String> lastNames = gson.fromJson(reader, LIST_NAMES_TYPE);
        setLastNames(lastNames);
    }

    private void readLocations() throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader(LOCATIONS_PATH));
        ArrayList<Location> locations = gson.fromJson(reader, LIST_LOCATIONS_TYPE);
        setLocations(locations);
    }

    public List<String> getMaleFirstNames() {
        return maleFirstNames;
    }

    public void setMaleFirstNames(ArrayList<String> maleFirstNames) {
        this.maleFirstNames = maleFirstNames;
    }

    public List<String> getFemaleFirstNames() {
        return femaleFirstNames;
    }

    public void setFemaleFirstNames(ArrayList<String> femaleFirstNames) {
        this.femaleFirstNames = femaleFirstNames;
    }

    public List<String> getLastNames() {
        return lastNames;
    }

    public void setLastNames(ArrayList<String> lastNames) {
        this.lastNames = lastNames;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }
}
