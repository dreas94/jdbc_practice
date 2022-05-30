package se.lexicon.dreas94.db;

import se.lexicon.dreas94.exception.DBConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection
{
    private static final String URL = "jdbc:mysql://localhost:3306/world";
    private static final String USER = "root";
    private static final String PASS = "xxx";

    public static Connection getConnection() throws DBConnectionException
    {
        Connection connection;
        try
        {
            connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("DONE");
        }
        catch (SQLException e)
        {
            throw new DBConnectionException(e.getMessage(), URL);
        }

        return connection;
    }

}
