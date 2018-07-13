package rbdavis.server.database.dataaccess.sql;

import database.dataaccess.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
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

    private Connection connection;

    public void openConnection() throws DAO.DatabaseException
    {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:spellcheck.sqlite";
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
}
