package rbdavis.shared.models;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.UUID;

public class AuthToken
{
    private String token;
    private String userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public AuthToken(String userId)
    {
        setUserId(userId);
    }

    public AuthToken(String token, String userId)
    {
        this(userId);
        this.setToken(token);
    }


    public AuthToken(String userId, LocalDateTime startTime)
    {
        setUserId(userId);
        setStartTime(startTime);
        setEndsAt();
    }

    public AuthToken(String token, String userId, LocalDateTime startTime)
    {
        this(userId, startTime);
        setToken(token);
    }

    public boolean isExpired()
    {
        if (startTime == null || endTime == null)
        {
            return false;
        }

        if (LocalDateTime.now().isAfter(endTime))
            return true;
        else
            return false;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        if (token == null)
        {
            this.token = UUID.randomUUID().toString();
        }
        else
        {
            this.token = token;
        }
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        if (userId == null)
        {
            throw new InvalidParameterException("UserId cannot be empty.");
        }
        this.userId = userId;
    }

    public LocalDateTime getStartTime()
    {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime)
    {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime()
    {
        return endTime;
    }

    public void setEndsAt()
    {
        if (startTime != null)
        {
            this.endTime = this.startTime.plusMinutes(30);
        }
    }
}
