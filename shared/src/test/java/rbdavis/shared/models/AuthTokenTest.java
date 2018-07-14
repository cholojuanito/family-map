package rbdavis.shared.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import rbdavis.shared.models.data.AuthToken;

import static org.junit.Assert.*;

public class AuthTokenTest
{
    private String token;
    private String userID;
    private LocalDateTime startTime;

    AuthToken tokenUnderTest;

    @Before
    public void setUp() throws Exception
    {
        token = UUID.randomUUID().toString();
        userID = UUID.randomUUID().toString();
        startTime = LocalDateTime.now();

        tokenUnderTest = new AuthToken(token, userID, startTime);
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

        tokenUnderTest = new AuthToken(token, userID, createdAt);
    }


    @Test
    public void testIsExpired()
    {
        assertFalse(tokenUnderTest.isExpired());

        try
        {
            Thread.sleep(10000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        assertTrue(tokenUnderTest.isExpired());
    }
}