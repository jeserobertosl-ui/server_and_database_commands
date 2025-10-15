package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection
{
    static Connection connect(String _db_name, String _user, String _password) throws SQLException
    {
        return DriverManager.getConnection
        (
            "jdbc:mysql://localhost:3306/" + _db_name, _user, _password
        );
    }

    static void close(Connection _connection)
    {
        try
        {
            if (_connection != null && !_connection.isClosed())
            {
                _connection.close();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}