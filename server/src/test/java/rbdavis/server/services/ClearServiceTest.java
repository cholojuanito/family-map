package rbdavis.server.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rbdavis.shared.models.http.responses.Response;
import static rbdavis.shared.utils.Constants.CLEAR_SUCCESS;

import static org.junit.Assert.*;

public class ClearServiceTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testClear() {
        Response clearResponse = ClearService.clear();
        assertEquals(CLEAR_SUCCESS, clearResponse.getMessage());
    }
}