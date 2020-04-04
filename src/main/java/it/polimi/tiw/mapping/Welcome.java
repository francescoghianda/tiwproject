package it.polimi.tiw.mapping;

import it.polimi.tiw.Application;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("")
public class Welcome extends HttpServlet
{

    public Welcome()
    {
        super();
    }

    @Override
    public void init() throws ServletException
    {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        HttpSession session = req.getSession();

        WebContext webContext = new WebContext(req, resp, getServletContext());
        webContext.setVariable("user", session.getAttribute("user"));
        Application.getTemplateEngine().process("index", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }
}
