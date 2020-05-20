package it.polimi.tiw.controllers.user;

import it.polimi.tiw.beans.User;
import it.polimi.tiw.beans.Worker;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.dao.UserDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/update-account")
public class EditUserInfoController extends HttpServlet
{
    private UserDao userDao;

    public EditUserInfoController()
    {
        super();
    }

    @Override
    public void init()
    {
        userDao = new UserDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        String email = Objects.toString(req.getParameter("email"), "");
        String password = Objects.toString(req.getParameter("password"), "");
        String expLvl = Objects.toString(req.getParameter("exp-lvl"), "");

        User user = (User) req.getSession().getAttribute("user");
        User newUser = new User(user.getId(), user.getUsername(), email, password, user.getRole());
        Worker worker = new Worker(user.getId(), expLvl, "data:image/jpeg;base64,");

        try
        {
            if(!userDao.updateUser(newUser, worker))
            {
                resp.sendError(500);
                return;
            }

            req.getSession().setAttribute("user", newUser);
            resp.setStatus(200);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            resp.sendError(500);
        }
        catch (InvalidBeanException e)
        {
            resp.sendError(400, e.toString());
        }

    }

}
