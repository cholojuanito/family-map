package rbdavis.shared.models;

public class User extends Model
{
    public final String TABLE = "Users";
    private String username;
    private String personId;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;

    public User()
    {
    }

    public User(String personId, String password, String email, String firstName, String lastName, Gender gender)
    {
        this.personId = personId;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public User(String username, String personId, String password, String email,
                    String firstName, String lastName, Gender gender)
    {
        this(personId, password, email, firstName, lastName, gender);
        this.username = username;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPersonId()
    {
        return personId;
    }

    public void setPersonId(String personId)
    {
        this.personId = personId;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
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
}
