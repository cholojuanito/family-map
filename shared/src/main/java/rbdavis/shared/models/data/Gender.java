package rbdavis.shared.models.data;

import com.google.gson.annotations.SerializedName;

/**
 * An enumeration of the 2 supported {@code Gender}s.
 * <p>
 * The enumeration generates a lower case "M" for males and a lower case "F"
 * for females.
 *
 *
 * @see User
 * @see Person
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */
public enum Gender {
    @SerializedName(value = "m", alternate = "M")
    M("m"),
    @SerializedName(value = "f", alternate = "F")
    F("f");

    private final String strVal;

    Gender(String val) {
        this.strVal = val;
    }

    @Override
    public String toString() {
        return strVal;
    }
}
