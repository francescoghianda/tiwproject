package it.polimi.tiw.mapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class Servlet4 extends HttpServlet
{
    public Servlet4()
    {
        super();
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        ServletOutputStream out = response.getOutputStream();

        out.println("Servlet 4");

        System.out.println("Servlet 4");

        out.flush();
    }
}
