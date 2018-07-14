package rbdavis.shared.models.data;

import java.security.InvalidParameterException;

/**
 * The {@code User} model represents the User table in the database.
 *
 * This model serves as a Data Transfer Object (DTO).
 *
 * @author  Tanner Davis
 * @version 0.1
 * @since   v0.1
 */
public class User
{
    public final String TABLE = "Users";
    private String username;
    private String personId;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;

    public User(String personId, String password, String email, String firstName,
                    String lastName, Gender gender)
    {
        setPersonId(personId);
        setPassword(password);
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
    }

    public User(String username, String personId, String password, String email,
                    String firstName, String lastName, Gender gender)
    {
        this(personId, password, email, firstName, lastName, gender);
        setUsername(username);
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        if (username == null)
        {
            throw new InvalidParameterException("Username cannot be empty.");
        }
        this.username = username;
    }

    public String getPersonId()
    {
        return personId;
    }

    public void setPersonId(String personId)
    {
        if (personId == null)
        {
            throw new InvalidParameterException("PersonId cannot be empty.");
        }
        this.personId = personId;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        if (password == null)
        {
            throw new InvalidParameterException("Password cannot be empty.");
        }
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        if (email == null)
        {
            throw new InvalidParameterException("Email cannot be empty.");
        }
        this.email = email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        if (firstName == null)
        {
            throw new InvalidParameterException("First name cannot be empty.");
    }
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        if (lastName == null)
        {
            throw new InvalidParameterException("Last name cannot be empty.");
        }
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
