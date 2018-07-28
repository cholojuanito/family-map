package rbdavis.shared.models.data;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;

/**
 * The {@code AuthToken} model represents the AuthToken table in the database.
 * This model serves as a Data Transfer Object (DTO).
 *
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class AuthToken {
    private String token;
    private String userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public AuthToken(String userId) {
        setUserId(userId);
    }

    public AuthToken(String token, String userId) {
        this(userId);
        setToken(token);
    }


    public AuthToken(String userId, LocalDateTime startTime) {
        setUserId(userId);
        setStartTime(startTime);
        setEndsAt();
    }

    public AuthToken(String token, String userId, LocalDateTime startTime) {
        this(userId, startTime);
        setToken(token);
    }

    public AuthToken(String token, String userId, LocalDateTime startTime, LocalDateTime endTime) {
        setToken(token);
        setUserId(userId);
        setStartTime(startTime);
        setEndTime(endTime);
    }

    /**
     * Compares the {@code AuthToken}'s endTime with
     * the current time. If the current time is after
     * the end time the token has expired.
     *
     * @return True if token is expired. False otherwise.
     */
    public boolean isExpired() {
        if (startTime == null || endTime == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(endTime))
            return true;
        else
            return false;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        if (token == null) {
            throw new InvalidParameterException("Token cannot be empty.");
        }
        else {
            this.token = token;
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        if (userId == null) {
            throw new InvalidParameterException("UserId cannot be empty.");
        }
        this.userId = userId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndsAt() {
        if (startTime != null) {
            this.endTime = this.startTime.plusMinutes(30);
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
