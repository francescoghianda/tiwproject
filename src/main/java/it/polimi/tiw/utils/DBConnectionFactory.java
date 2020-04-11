package it.polimi.tiw.utils;

import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Deprecated
public class DBConnectionFactory
{
    private String url;
    private String user;
    private String password;

    public DBConnectionFactory(ServletContext context) throws ClassNotFoundException
    {
        String driver = context.getInitParameter("dbDriver");
        url = context.getInitParameter("dbUrl");
        user = context.getInitParameter("dbUser");
        password = context.getInitParameter("dbPassword");

        Class.forName(driver);
    }

    public Connection getConnection() throws UnavailableException
    {
        try
        {
            return DriverManager.getConnection(url, user, password);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new UnavailableException("Can't establish a connection with the database!");
        }
    }
}
