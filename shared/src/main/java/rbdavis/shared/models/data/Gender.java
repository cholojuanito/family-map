package rbdavis.shared.models.data;

/**
 * An enumeration of the two {@code Gender}s that are allowed in the application.
 *
 * The enumeration generates a lower case "M" for males and a lower case "F"
 * for females.
 *
 * Used in:
 * {@code User} model
 * {@code Person} model
 *
 * @author  Tanner Davis
 * @version 0.1
 * @since   v0.1
 */
public enum Gender
{
    M("m"), F("f");

    private final String strVal;

    Gender(String val)
    {
        this.strVal = val;
    }

    @Override
    public String toString()
    {
        return strVal;
    }
}
