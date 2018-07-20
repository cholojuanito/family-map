package rbdavis.server.database.sql.dataaccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlConnectionManager;
import rbdavis.shared.models.data.AuthToken;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AuthTokenSqlDAOTest {
    private Connection connection;
    final boolean doNotCommit = false;
    private final String token1 = "8e231070-d4a4-404e-b50b-30135c935a8a"; //UUID.randomUUID().toString();
    private final String token2 = "8e2c071-d5p4-414e-b50n-y9735c935b8a";
    private final String username1 = "happy"; //UUID.randomUUID().toString();
    private final String username2 = "cholo";

    private final AuthToken auth1 = new AuthToken(token1, username1, LocalDateTime.now());
    private final AuthToken auth2 = new AuthToken(token2, username2, LocalDateTime.now());

    private AuthTokenSqlDAO daoUnderTest;

    @Before
    public void setUp() throws Exception {
        try {
            connection = SqlConnectionManager.openUnitTestConnection();
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new DAO.DatabaseException("Test database connection failed");
        }

        daoUnderTest = new AuthTokenSqlDAO(connection);
    }

    @After
    public void tearDown() throws Exception {
        SqlConnectionManager.closeConnection(connection, doNotCommit);
        daoUnderTest = null;
    }

    @Test
    public void testCreateAndFind() {
        try {
            daoUnderTest.create(auth1);
            daoUnderTest.create(auth2);
            AuthToken foundToken1 = daoUnderTest.findById(token1);
            AuthToken foundToken2 = daoUnderTest.findById(token2);

            assertNotNull(foundToken1);
            assertNotNull(foundToken2);
            assertEquals(token1, foundToken1.getToken());
            assertEquals(username1, foundToken1.getUserId());
            assertEquals(token2, foundToken2.getToken());
            assertEquals(username2, foundToken2.getUserId());
        }
        catch (DAO.DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdate() {
        // Not supported
    }

    @Test
    public void testDelete() {
        try {
            daoUnderTest.create(auth1);
            daoUnderTest.create(auth2);

            assertTrue(daoUnderTest.delete(token2));

            AuthToken foundToken2 = daoUnderTest.findById(token2);
            assertNull(foundToken2);

        }
        catch (DAO.DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAll() {
        try {

            daoUnderTest.create(auth1);
            daoUnderTest.create(auth2);

            List<AuthToken> tokens = daoUnderTest.all();

            assertTrue(tokens.size() == 2);
        }
        catch (DAO.DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteAll() {
        try {

            daoUnderTest.create(auth1);
            daoUnderTest.create(auth2);

            assertTrue(daoUnderTest.deleteAll());

            List<AuthToken> tokens = daoUnderTest.all();

            assertTrue(tokens.size() == 0);
        }
        catch (DAO.DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindByUserId() {
        try {

            daoUnderTest.create(auth1);
            daoUnderTest.create(auth2);

            AuthToken foundToken = daoUnderTest.findByUserId(username1);

            assertNotNull(foundToken);
            assertEquals(token1, foundToken.getToken());
        }
        catch (DAO.DatabaseException e) {
            e.printStackTrace();
        }
    }
}