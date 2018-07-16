package rbdavis.server.database.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import rbdavis.server.database.DAO;

/**
 * A {@code SqlConnectionManager} generates a {@code Connection} to a SQL
 * database. It is up to each {@code DAO} implementation to close the {@code Connection}
 * that is generated.
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
    private static final String DB_URL = "jdbc:sqlite:database" + File.separator + "family_map.db";
    //URL to the unit test database
    private static final String TEST_DB_URL = "jdbc:sqlite:database" + File.separator + "test.db";

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
            throw new DAO.DatabaseException("openConnection failed", e);
        }
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
