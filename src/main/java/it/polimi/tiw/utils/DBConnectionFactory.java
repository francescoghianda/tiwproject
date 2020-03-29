package it.polimi.tiw.utils;

import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionFactory
{

    private DBConnectionFactory()
    {

    }


    public static Connection getConnection(ServletContext context) throws UnavailableException
    {
        try
        {
            String driver = context.getInitParameter("dbDriver");
            String url = context.getInitParameter("dbUrl");
            String user = context.getInitParameter("dbUser");
            String password = context.getInitParameter("dbPassword");
            Class.forName(driver);
            return DriverManager.getConnection(url, user, password);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            throw new UnavailableException("Database driver not found!");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new UnavailableException("Can't establish a connection with the database!");
        }
    }
}
