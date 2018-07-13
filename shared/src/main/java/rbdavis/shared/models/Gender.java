package rbdavis.shared.models;

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
