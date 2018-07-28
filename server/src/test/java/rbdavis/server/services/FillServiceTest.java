package rbdavis.server.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rbdavis.shared.models.http.requests.FillRequest;
import rbdavis.shared.models.http.responses.Response;

import static org.junit.Assert.assertEquals;

public class FillServiceTest {
    private String username = "cholo";
    private int threeGens = 3;
    private int fiveGens = 5;

    @Before
    public void setUp() throws Exception {
        LoadServiceTest loader = new LoadServiceTest();
        loader.setUp();
        loader.testLoad();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testFill() {
        FillRequest request4Gens = new FillRequest(username);
        FillRequest request3Gens = new FillRequest(username, threeGens);
        FillRequest request5Gens = new FillRequest(username, fiveGens);

        Response response4Gens = new FillService().fill(request4Gens);
        Response response3Gens = new FillService().fill(request3Gens);
        Response response5Gens = new FillService().fill(request5Gens);


        assertEquals(response3Gens.getMessage(), "Added 15 people and 44 events to the database");
        assertEquals(response4Gens.getMessage(), "Added 31 people and 108 events to the database");
        assertEquals(response5Gens.getMessage(), "Added 63 people and 236 events to the database");
    }
}