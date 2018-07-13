package rbdavis.shared.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class AuthTokenTest
{
    private String token;
    private String userID;
    private LocalDateTime createdAt;
    private LocalDateTime endsAt;

    AuthToken tokenUnderTest;

    @Before
    public void setUp() throws Exception
    {
        token = UUID.randomUUID().toString();
        userID = UUID.randomUUID().toString();
        createdAt = LocalDateTime.now();
        endsAt = createdAt.plusSeconds(5);

        tokenUnderTest = new AuthToken(token, userID, createdAt, endsAt);
    }

    @After
    public void tearDown() throws Exception
    {
        tokenUnderTest = null;
    }

    @Test
    public void testConstructor()
    {
        String token = UUID.randomUUID().toString();
        String userID = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime endsAt = createdAt.plusSeconds(10);

        tokenUnderTest = new AuthToken(token, userID, createdAt, endsAt);
    }


    @Test
    public void testIsExpired()
    {
        assertFalse(tokenUnderTest.isExpired());

        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        assertTrue(tokenUnderTest.isExpired());
    }
}