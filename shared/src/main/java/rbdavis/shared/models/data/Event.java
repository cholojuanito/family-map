package rbdavis.shared.models.data;

import com.google.gson.annotations.SerializedName;

import java.security.InvalidParameterException;
import java.time.LocalDate;

/**
 * The {@code Event} model represents the Event table in the database.
 * <p>
 * This model serves as a Data Transfer Object (DTO).
 * <p>
 * Each {@code Event} must be an {@code EventType}.
 * The {@code EventType}s are as follows:
 * <ul>
 * <li>Birth</li>
 * <li>Baptism</li>
 * <li>Marriage</li>
 * <li>Death</li>
 * </ul>
 *
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class Event {
    @SerializedName(value = "id", alternate = "eventID")
    private String id;
    @SerializedName(value = "personId", alternate = "personID")
    private String personId;
    @SerializedName(value = "userId", alternate = {"descendant", "userName", "userID"})
    private String userId;
    @SerializedName(value = "type", alternate = "eventType")
    private EventType type;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("city")
    private String city;
    @SerializedName("country")
    private String country;
    @SerializedName(value = "dateHappened", alternate = "year")
    private LocalDate dateHappened;

    public Event(String personId, String userId, EventType type, String latitude, String longitude,
                 String city, String country, LocalDate dateHappened) {
        setPersonId(personId);
        setUserId(userId);
        setType(type);
        setLatitude(latitude);
        setLongitude(longitude);
        setCity(city);
        setCountry(country);
        setDateHappened(dateHappened);
    }


    public Event(String personId, String userId, String typeStr, String latitude, String longitude,
                 String city, String country, LocalDate dateHappened) {
        setPersonId(personId);
        setUserId(userId);
        setType(typeStr);
        setLatitude(latitude);
        setLongitude(longitude);
        setCity(city);
        setCountry(country);
        setDateHappened(dateHappened);
    }


    public Event(String id, String personId, String userId, EventType type, String latitude,
                 String longitude, String city, String country, LocalDate dateHappened) {
        this(personId, userId, type, latitude, longitude, city, country, dateHappened);
        setId(id);
    }


    public Event(String id, String personId, String userId, String typeStr, String latitude,
                 String longitude, String city, String country, LocalDate dateHappened) {
        this(personId, userId, typeStr, latitude, longitude, city, country, dateHappened);
        setId(id);
    }

    public Event(Event other) {
        this.personId = other.personId;
        this.userId = other.userId;
        this.type = other.type;
        this.latitude = other.latitude;
        this.longitude = other.longitude;
        this.city = other.city;
        this.country = other.country;
        this.dateHappened = other.dateHappened;
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

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        if (personId == null) {
            throw new InvalidParameterException("Id cannot be empty.");
        }
        this.personId = personId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        if (userId == null) {
            throw new InvalidParameterException("Id cannot be empty.");
        }
        this.userId = userId;
    }

    public EventType getType() {
        return type;
    }

    public void setType(String typeStr) {
        switch (typeStr.toLowerCase()) {
            case "birth":
                this.type = EventType.BIRTH;
                break;
            case "baptism":
                this.type = EventType.BAPTISM;
                break;
            case "marriage":
                this.type = EventType.MARRIAGE;
                break;
            case "death":
                this.type = EventType.DEATH;
                break;
            default:
                this.type = null;
                break;
        }
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        if (latitude == null) {
            throw new InvalidParameterException("Id cannot be empty.");
        }
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        if (longitude == null) {
            throw new InvalidParameterException("Id cannot be empty.");
        }
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (city == null) {
            throw new InvalidParameterException("Id cannot be empty.");
        }
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        if (country == null) {
            throw new InvalidParameterException("Id cannot be empty.");
        }
        this.country = country;
    }

    public LocalDate getDateHappened() {
        return dateHappened;
    }

    public void setDateHappened(LocalDate dateHappened) {
        this.dateHappened = dateHappened;
    }

    /**
     * An enumeration of the 4 supported event types.
     * The {@code EventType}s are as follows:
     * <ul>
     * <li>Birth</li>
     * <li>Baptism</li>
     * <li>Marriage</li>
     * <li>Death</li>
     * </ul>
     *
     * @see Event
     * @author Tanner Davis
     * @version 0.1
     * @since v0.1
     */

    public enum EventType {
        @SerializedName(value = "birth", alternate = {"BIRTH", "Birth"})
        BIRTH("Birth"),
        @SerializedName(value = "baptism", alternate = {"BAPTISM", "Baptism"})
        BAPTISM("Baptism"),
        @SerializedName(value = "marriage", alternate = {"MARRIAGE", "Marriage"})
        MARRIAGE("Marriage"),
        @SerializedName(value = "death", alternate = {"DEATH", "Death"})
        DEATH("Death");

        private final String strVal;

        EventType(String val) {
            this.strVal = val;
        }

        @Override
        public String toString() {
            return strVal;
        }
    }

}
