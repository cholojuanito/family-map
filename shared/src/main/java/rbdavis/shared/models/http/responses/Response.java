package rbdavis.shared.models.http.responses;

public class Response
{
    String message;

    public Response(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
