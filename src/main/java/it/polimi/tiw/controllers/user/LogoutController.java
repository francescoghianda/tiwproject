package it.polimi.tiw.controllers.user;

import it.polimi.tiw.authentication.AuthenticationHelper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet
{

    public LogoutController()
    {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        AuthenticationHelper.invalidateAuthentication(request);
        response.sendRedirect("/");
    }
}
