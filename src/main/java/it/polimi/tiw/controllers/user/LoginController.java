package it.polimi.tiw.controllers.user;

import it.polimi.tiw.authentication.AuthenticationHelper;
import it.polimi.tiw.Application;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginController extends HttpServlet
{

    public LoginController()
    {
        super();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)throws IOException
    {
        WebContext webContext = new WebContext(req, resp, getServletContext(), req.getLocale());

        webContext.setVariable("invalid", req.getAttribute("invalid-credentials"));

        String redirect = (String)req.getAttribute("redirect");
        webContext.setVariable("redirect", redirect != null ? redirect : "/");

        Application.getTemplateEngine().process("login", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        try
        {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            String redirect = req.getParameter("redirect");

            if(AuthenticationHelper.authenticate(req, username, password))resp.sendRedirect(redirect != null ? redirect : "/");
            else
            {
                req.setAttribute("invalid-credentials", true);
                doGet(req, resp);
            }
        }
        catch (SQLException | UnavailableException e)
        {
            e.printStackTrace();
            throw new UnavailableException("Error");
        }
    }

}
