package rbdavis.server.database.dataaccess.sql;

import rbdavis.server.database.dataaccess.DAO;
import rbdavis.shared.models.User;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlDAO
{
    static
    {
        try
        {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private Connection connection = null;

    private final String DB_NAME = "database" +  File.separator + "family_map.db";

    private final String TEST_DB_NAME = "database" +  File.separator + "test.db";

    public void openConnection() throws DAO.DatabaseException
    {
        try
        {
            final String CONNECTION_URL = "jdbc:sqlite:" + DB_NAME;
            // Open a database connection
            connection = DriverManager.getConnection(CONNECTION_URL);
            // Start a transaction
            connection.setAutoCommit(false);
        }
        catch (SQLException e)
        {
            throw new DAO.DatabaseException("openConnection failed", e);
        }
    }

    public void closeConnection(boolean commit) throws DAO.DatabaseException
    {
        try {
            if (commit)
            {
                connection.commit();
            }
            else
            {
                connection.rollback();
            }

            connection.close();
            connection = null;
        }
        catch (SQLException e)
        {
            throw new DAO.DatabaseException("closeConnection failed", e);
        }
    }

    public void createAUser(User user) throws DAO.DatabaseException
    {
        try
        {
            PreparedStatement stmt = null;
            this.openConnection();
            try
            {
                String sql = "INSERT INTO " + user.TABLE +
                             "(username, person_id, password, email, first_name, last_name, gender)" +
                             "VALUES (?, ?, ?, ?, ?, ?, ?)";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPersonId());
                stmt.setString(3, user.getPassword());
                stmt.setString(4, user.getEmail());
                stmt.setString(5, user.getFirstName());
                stmt.setString(6, user.getLastName());
                stmt.setString(7, user.getGender().toString());

                if (stmt.executeUpdate() != 1)
                {
                    throw new DAO.DatabaseException("Create user failed.");
                }
                else
                {
                    System.out.println("It worked!");
                }
            }
            finally
            {
                if (stmt != null)
                {
                    stmt.close();
                }
            }

            this.closeConnection(true);
        }
        catch (SQLException e)
        {
            throw new DAO.DatabaseException("createUser failed " + e.getMessage(), e);
        }
    }
}
