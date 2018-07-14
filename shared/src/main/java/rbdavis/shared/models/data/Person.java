package rbdavis.shared.models.data;

import java.security.InvalidParameterException;

/**
 * The {@code Person} model represents the Person table in the database.
 * <p>
 * This model serves as a Data Transfer Object (DTO).
 * <p>
 * A {@code Person} is a part of a Family Tree and can (but does not need to)
 * therefore be connected to 3 other {@code Person}s:
 * <ul>
 * <li>Father</li>
 * <li>Mother</li>
 * <li>Spouse</li>
 * </ul>
 *
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class Person {
    private String id;
    private String userId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String fatherId = null;
    private String motherID = null;
    private String spouseID = null;

    public Person(String userId, String firstName, String lastName, Gender gender,
                  String fatherId, String motherID, String spouseID) {
        setUserId(userId);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setFatherId(fatherId);
        setMotherID(motherID);
        setSpouseID(spouseID);
    }

    public Person(String id, String userId, String firstName, String lastName, Gender gender,
                  String fatherId, String motherID, String spouseID) {
        this(userId, firstName, lastName, gender, fatherId, motherID, spouseID);
        setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null) {
            throw new InvalidParameterException("Id cannot be empty.");
        }
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        if (userId == null) {
            throw new InvalidParameterException("UserId cannot be empty.");
        }
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null) {
            throw new InvalidParameterException("First name cannot be empty.");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null) {
            throw new InvalidParameterException("Last name cannot be empty.");
        }
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }
}
