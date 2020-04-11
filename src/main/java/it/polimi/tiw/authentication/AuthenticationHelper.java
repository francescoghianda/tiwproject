package it.polimi.tiw.authentication;

import it.polimi.tiw.authentication.security.PBKDF2WithHmacSHA512;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.UserDAO;

import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public class AuthenticationHelper
{
    private static final String AUTH_ATTRIBUTE = "auth";
    private static final String USER_ATTRIBUTE = "user";

    private AuthenticationHelper() {}

    public static boolean isAuthenticated(HttpSession session)
    {
        return !(session == null || session.getAttribute(AUTH_ATTRIBUTE) == null || !((boolean)session.getAttribute(AUTH_ATTRIBUTE)));
    }

    public static void invalidateAuthentication(HttpServletRequest request)
    {
        request.getSession().invalidate();
    }

    public static boolean authenticate(HttpServletRequest request, String username, String password) throws UnavailableException, SQLException
    {
        UserDAO dao = new UserDAO();
        User user = dao.findUserByUsername(username);
        if(user == null || !PBKDF2WithHmacSHA512.getInstance().validate(password, user.getPassword()))return false;

        HttpSession session = request.getSession();
        session.setAttribute(AUTH_ATTRIBUTE, true);
        session.setAttribute(USER_ATTRIBUTE, user);

        return true;
    }

}
