package rbdavis.shared.models;

import java.security.InvalidParameterException;
import java.time.LocalDate;

public class Event
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
    private LocalDate dateHappened;

    public Event(String personId, String userId, EventType type, String latitude, String longitude,
                    String city, String country, LocalDate dateHappened)
    {
        setPersonId(personId);
        setUserId(userId);
        setType(type);
        setLatitude(latitude);
        setLongitude(longitude);
        setCity(city);
        setCountry(country);
        setDateHappened(dateHappened);
    }

    public Event(String id, String personId, String userId, EventType type, String latitude,
                    String longitude, String city, String country, LocalDate dateHappened)
    {
        this(personId, userId, type, latitude, longitude, city, country, dateHappened);
        setId(id);
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        if (id == null)
        {
            throw new InvalidParameterException("Id cannot be empty.");
        }
        this.id = id;
    }

    public String getPersonId()
    {
        return personId;
    }

    public void setPersonId(String personId)
    {
        if (personId == null)
        {
            throw new InvalidParameterException("Id cannot be empty.");
        }
        this.personId = personId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        if (userId == null)
        {
            throw new InvalidParameterException("Id cannot be empty.");
        }
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
        if (latitude == null)
        {
            throw new InvalidParameterException("Id cannot be empty.");
        }
        this.latitude = latitude;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        if (longitude == null)
        {
            throw new InvalidParameterException("Id cannot be empty.");
        }
        this.longitude = longitude;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        if (city == null)
        {
            throw new InvalidParameterException("Id cannot be empty.");
        }
        this.city = city;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        if (country == null)
        {
            throw new InvalidParameterException("Id cannot be empty.");
        }
        this.country = country;
    }

    public LocalDate getDateHappened()
    {
        return dateHappened;
    }

    public void setDateHappened(LocalDate dateHappened)
    {
        this.dateHappened = dateHappened;
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
