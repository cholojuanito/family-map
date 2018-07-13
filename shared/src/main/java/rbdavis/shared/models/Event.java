package rbdavis.shared.models;

import java.time.LocalDate;
import java.util.UUID;

public class Event extends Model
{
    public final String TABLE = "Events";
    private String id;
    private String personId;
    private String userId;
    private EventType type;
    private String latitude;
    private String longitude;
    private String city;
    private String country;
    private LocalDate happenedOn;

    public Event()
    {
    }

    public Event(String personId, String userId, EventType type, String latitude, String longitude,
                 String city, String country, LocalDate happenedOn)
    {
        this.personId = personId;
        this.userId = userId;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.country = country;
        this.happenedOn = happenedOn;
    }

    public Event(String id, String personId, String userId, EventType type, String latitude, String longitude,
                        String city, String country, LocalDate happenedOn)
    {
        this(personId, userId, type, latitude, longitude, city, country, happenedOn);
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPersonId()
    {
        return personId;
    }

    public void setPersonId(String personId)
    {
        this.personId = personId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public EventType getType()
    {
        return type;
    }

    public void setType(EventType type)
    {
        this.type = type;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public LocalDate getHappenedOn()
    {
        return happenedOn;
    }

    public void setHappenedOn(LocalDate happenedOn)
    {
        this.happenedOn = happenedOn;
    }

    public enum EventType
    {
        BIRTH("Birth"),
        BAPTISM("Baptism"),
        MARRIAGE("Marriage"),
        DEATH("Death");

        private final String strVal;

        EventType(String val)
        {
            this.strVal = val;
        }

        @Override
        public String toString()
        {
            return strVal;
        }
    }
}
