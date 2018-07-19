package rbdavis.server.database.sql;

import java.sql.Connection;
import java.sql.SQLException;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.dataaccess.AuthTokenSqlDAO;
import rbdavis.server.database.sql.dataaccess.EventSqlDAO;
import rbdavis.server.database.sql.dataaccess.PersonSqlDAO;
import rbdavis.server.database.sql.dataaccess.UserSqlDAO;

public class SqlDatabase {
    // Dao's
    private UserSqlDAO userDao;
    private PersonSqlDAO personDao;
    private EventSqlDAO eventDao;
    private AuthTokenSqlDAO authTokenDao;
    // Connection
    private Connection connection;

    public SqlDatabase() throws DAO.DatabaseException {
        try {
            connection = SqlConnectionManager.openConnection();
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new DAO.DatabaseException("SQL database object creation failed", e);
        }
        userDao = new UserSqlDAO(connection);
        personDao = new PersonSqlDAO(connection);
        eventDao = new EventSqlDAO(connection);
        authTokenDao = new AuthTokenSqlDAO(connection);
    }


    public void startTransaction() throws DAO.DatabaseException {
        connection = SqlConnectionManager.openConnection();
    }

    public void endTransaction(boolean commit) throws DAO.DatabaseException {
        SqlConnectionManager.closeConnection(connection, commit);
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
