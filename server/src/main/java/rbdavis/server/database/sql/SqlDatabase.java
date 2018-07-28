package rbdavis.server.database.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.dataaccess.AuthTokenSqlDAO;
import rbdavis.server.database.sql.dataaccess.EventSqlDAO;
import rbdavis.server.database.sql.dataaccess.PersonSqlDAO;
import rbdavis.server.database.sql.dataaccess.UserSqlDAO;
import static rbdavis.shared.utils.Constants.*;

public class SqlDatabase {

    private UserSqlDAO userDao;
    private PersonSqlDAO personDao;
    private EventSqlDAO eventDao;
    private AuthTokenSqlDAO authTokenDao;

    private Connection connection;

    static Logger logger;

    static {
        try {
            initLog();
        }
        catch (IOException e) {
            System.out.println(INIT_LOG_ERR + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initLog() throws IOException {

        Level logLevel = Level.ALL;

        logger = Logger.getLogger(DAO_LOG);
        logger.setLevel(logLevel);
        logger.setUseParentHandlers(false);

        java.util.logging.FileHandler databaseFileHandler = new java.util.logging.FileHandler(DAO_LOG_PATH, false);
        databaseFileHandler.setLevel(logLevel);
        databaseFileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(databaseFileHandler);
    }

    public SqlDatabase() throws DAO.DatabaseException {
        try {
            connection = SqlConnectionManager.openConnection();
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            logger.warning(DB_CONN_FAILED + e.getMessage());
            throw new DAO.DatabaseException(DB_CONN_FAILED);
        }
        userDao = new UserSqlDAO(connection);
        personDao = new PersonSqlDAO(connection);
        eventDao = new EventSqlDAO(connection);
        authTokenDao = new AuthTokenSqlDAO(connection);
        logger.info(DB_CONN_OPENED);
    }

    public void endTransaction(boolean commit) throws DAO.DatabaseException {
        SqlConnectionManager.closeConnection(connection, commit);
        logger.info(DB_CONN_CLOSED);
    }

    public UserSqlDAO getUserDao() {
        return userDao;
    }

    public void setUserDao(UserSqlDAO userDao) {
        this.userDao = userDao;
    }

    public PersonSqlDAO getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonSqlDAO personDao) {
        this.personDao = personDao;
    }

    public EventSqlDAO getEventDao() {
        return eventDao;
    }

    public void setEventDao(EventSqlDAO eventDao) {
        this.eventDao = eventDao;
    }

    public AuthTokenSqlDAO getAuthTokenDao() {
        return authTokenDao;
    }

    public void setAuthTokenDao(AuthTokenSqlDAO authTokenDao) {
        this.authTokenDao = authTokenDao;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
