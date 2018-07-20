package rbdavis.server.database.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.xml.crypto.Data;

import rbdavis.server.database.DAO;

/**
 * A {@code SqlConnectionManager} manages a {@code Connection} to a SQL
 * database. All methods are static.
 *
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class SqlConnectionManager {
    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //URL to the database
    private static final String DB_URL = "jdbc:sqlite:server" + File.separator  + "database" + File.separator + "family_map.db";
    //URL to the unit test database
    private static final String TEST_DB_URL = "jdbc:sqlite:server" + File.separator  + "database" + File.separator + "test.db";

    /**
     * Creates a new {@code Connection} through the {@code DriverManager}
     * to the main database
     *
     * @return A {@code Connection} to a SQL database
     * @throws DAO.DatabaseException Any issue with establishing connection is thrown
     */
    public static Connection openConnection() throws DAO.DatabaseException {
        try {
            // Open a database connection
            return DriverManager.getConnection(DB_URL);

        } catch (SQLException e) {
            throw new DAO.DatabaseException("openConnection failed", e);
        }
    }

    /**
     * Creates a new {@code Connection} through the {@code DriverManager}
     * to the unit test database.
     *
     * @return A {@code Connection} to a SQL database
     * @throws DAO.DatabaseException Any issue with establishing connection is thrown
     */
    public static Connection openUnitTestConnection() throws DAO.DatabaseException {
        try {
            // Open a database connection
            return DriverManager.getConnection(TEST_DB_URL);

        } catch (SQLException e) {
            throw new DAO.DatabaseException("openTestConnection failed", e);
        }
    }

    public static void closeConnection(Connection connection, boolean commit) throws DAO.DatabaseException {
        try {
            if (connection != null) {
                if (commit) {
                    connection.commit();
                }
                else {
                    connection.rollback();
                }
                connection.close();
            }
        }
        catch (SQLException e) {
            throw new DAO.DatabaseException("Unable to close connection", e);
        }
    }
}
