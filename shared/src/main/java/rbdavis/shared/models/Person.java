package rbdavis.shared.models;

public class Person extends Model
{
    private String id;
    private String userId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String fatherId = null;
    private String motherID = null;
    private String spouseID = null;

    public Person()
    {
    }

    public Person(String userId, String firstName, String lastName, Gender gender,
                  String fatherId, String motherID, String spouseID) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherId = fatherId;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public Person(String id, String userId, String firstName, String lastName, Gender gender,
                  String fatherId, String motherID, String spouseID)
    {
        this(userId, firstName, lastName, gender, fatherId, motherID, spouseID);
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

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public Gender getGender()
    {
        return gender;
    }

    public void setGender(Gender gender)
    {
        this.gender = gender;
    }

    public String getFatherId()
    {
        return fatherId;
    }

    public void setFatherId(String fatherId)
    {
        this.fatherId = fatherId;
    }

    public String getMotherID()
    {
        return motherID;
    }

    public void setMotherID(String motherID)
    {
        this.motherID = motherID;
    }

    public String getSpouseID()
    {
        return spouseID;
    }

    public void setSpouseID(String spouseID)
    {
        this.spouseID = spouseID;
    }
}
