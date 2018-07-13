package rbdavis.shared.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuthToken extends Model
{
    private String token;
    private String userID;
    private LocalDateTime createdAt;
    private LocalDateTime endsAt;

    public AuthToken()
    {
    }

    public AuthToken(String userID, LocalDateTime createdAt, LocalDateTime endsAt)
    {
        this.userID = userID;
        this.createdAt = createdAt;
        this.endsAt = endsAt;
    }

    public AuthToken(String token, String userID, LocalDateTime createdAt, LocalDateTime endsAt)
    {
        this(userID, createdAt, endsAt);
        this.token = token;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public LocalDateTime getEndsAt()
    {
        return endsAt;
    }

    public void setEndsAt(LocalDateTime endsAt)
    {
        this.endsAt = endsAt;
    }
}
