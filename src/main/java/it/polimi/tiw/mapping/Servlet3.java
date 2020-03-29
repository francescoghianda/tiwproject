package it.polimi.tiw.mapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class Servlet3 extends HttpServlet
{
    public Servlet3()
    {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        ServletOutputStream out = response.getOutputStream();

        System.out.println("Servlet 3");

        out.println("Servlet 3");
    }
}
