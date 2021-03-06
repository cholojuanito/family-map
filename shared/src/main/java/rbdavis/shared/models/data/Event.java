package rbdavis.shared.models.data;

import com.google.gson.annotations.SerializedName;

import java.security.InvalidParameterException;
import java.time.LocalDate;

import rbdavis.shared.utils.Constants;

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

public class Event implements Comparable<Event> {
    @SerializedName(value = "id", alternate = "eventID")
    private String id;
    @SerializedName(value = "personId", alternate = "personID")
    private String personId;
    @SerializedName(value = "userId", alternate = {"descendant", "userName", "userID"})
    private String userId;
    @SerializedName("eventType")
    private String eventType;
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

    public Event(String personId, String userId, String typeStr, String latitude, String longitude,
                 String city, String country, LocalDate dateHappened) {
        setPersonId(personId);
        setUserId(userId);
        setEventType(typeStr);
        setLatitude(latitude);
        setLongitude(longitude);
        setCity(city);
        setCountry(country);
        setDateHappened(dateHappened);
    }

    public Event(String id, String personId, String userId, String typeStr, String latitude,
                 String longitude, String city, String country, LocalDate dateHappened) {
        this(personId, userId, typeStr, latitude, longitude, city, country, dateHappened);
        setId(id);
    }

    public Event(Event other) {
        this.personId = other.personId;
        this.userId = other.userId;
        this.eventType = other.eventType;
        this.latitude = other.latitude;
        this.longitude = other.longitude;
        this.city = other.city;
        this.country = other.country;
        this.dateHappened = other.dateHappened;
    }

    @Override
    public int compareTo(Event other) {
        if (other.getEventType().toLowerCase().equals("birth")) {
            return 1;
        }

        if (other.getEventType().toLowerCase().equals("death")) {
            return -1;
        }

        if (other.getDateHappened().getYear() > this.getDateHappened().getYear()) {
            return -1;
        }
        else if (other.getDateHappened().getYear() < this.getDateHappened().getYear()) {
            return 1;
        }
        else {
            return other.getEventType().toLowerCase().compareTo(this.getEventType().toLowerCase());
        }
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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
}
