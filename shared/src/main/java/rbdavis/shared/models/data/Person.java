package rbdavis.shared.models.data;

import com.google.gson.annotations.SerializedName;

import java.security.InvalidParameterException;

/**
 * The {@code Person} model represents the Person table in the database.
 * <p>
 * This model serves as a Data Transfer Object (DTO).
 * <p>
 * A {@code Person} is a part of a Family Tree and can (but does not need to)
 * be connected to 3 other {@code Person}s:
 * <ul>
 * <li>Father</li>
 * <li>Mother</li>
 * <li>Spouse</li>
 * </ul>
 *
 * @author Tanner Davis
 * @version 0.1
 * @see Gender
 * @since v0.1
 */

public class Person {
    @SerializedName(value = "id", alternate = {"personId", "personID"})
    private String id;
    @SerializedName(value = "userId", alternate = {"descendant", "userName", "userID"})
    private String userId;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("gender")
    private Gender gender;
    @SerializedName(value = "fatherId", alternate = {"fatherID", "father"})
    private String fatherId = null;
    @SerializedName(value = "motherId", alternate = {"motherID", "mother"})
    private String motherId = null;
    @SerializedName(value = "spouseId", alternate = {"spouseID", "spouse"})
    private String spouseId = null;

    public Person(String userId, String firstName, String lastName, Gender gender,
                  String fatherId, String motherId, String spouseId) {
        setUserId(userId);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setFatherId(fatherId);
        setMotherId(motherId);
        setSpouseId(spouseId);
    }

    public Person(String userId, String firstName, String lastName, String genderStr,
                  String fatherId, String motherId, String spouseId) {
        setUserId(userId);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(genderStr);
        setFatherId(fatherId);
        setMotherId(motherId);
        setSpouseId(spouseId);
    }

    public Person(String id, String userId, String firstName, String lastName, Gender gender,
                  String fatherId, String motherId, String spouseId) {
        this(userId, firstName, lastName, gender, fatherId, motherId, spouseId);
        setId(id);
    }

    public Person(String id, String userId, String firstName, String lastName, String genderStr,
                  String fatherId, String motherId, String spouseId) {
        this(userId, firstName, lastName, genderStr, fatherId, motherId, spouseId);
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

    public void setGender(String genderStr) {
        switch (genderStr.toLowerCase()) {
            case "m":
                this.gender = Gender.M;
                break;
            case "f":
                this.gender = Gender.F;
                break;
            default:
                this.gender = null;
                break;
        }
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

    public String getMotherId() {
        return motherId;
    }

    public void setMotherId(String motherId) {
        this.motherId = motherId;
    }

    public String getSpouseId() {
        return spouseId;
    }

    public void setSpouseId(String spouseId) {
        this.spouseId = spouseId;
    }
}
