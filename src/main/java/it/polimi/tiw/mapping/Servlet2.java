package it.polimi.tiw.mapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/dbtest")
public class Servlet2 extends HttpServlet
{

    private Connection connection;

    public Servlet2()
    {
        super();
    }

    @Override
    public void init() throws UnavailableException
    {
        try
        {
            String driver = getServletContext().getInitParameter("dbDriver");
            String url =  getServletContext().getInitParameter("dbUrl");
            String user =  getServletContext().getInitParameter("dbUser");
            String password =  getServletContext().getInitParameter("dbPassword");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        }
        catch (ClassNotFoundException e)
        {
            throw new UnavailableException("Can't load database driver");
        }
        catch (SQLException e)
        {
            throw new UnavailableException("Couldn't get db connection");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        ServletOutputStream out = response.getOutputStream();
        if(connection == null)out.print("Connection Error!");
        else out.print("OK!");
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.sendError(405);
    }

}
