package it.polimi.tiw.controllers.user;

import it.polimi.tiw.Application;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.beans.Worker;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.dao.UserDao;
import it.polimi.tiw.utils.ProfilePictureGenerator;
import it.polimi.tiw.utils.beans.UserRoles;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/create-user")
public class CreateUserController extends HttpServlet
{
    private UserDao dao;

    public CreateUserController()
    {
        super();
    }

    @Override
    public void init() throws ServletException
    {
        dao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        WebContext webContext = new WebContext(req, resp, getServletContext(), req.getLocale());
        Application.getTemplateEngine().process("create-user", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String username = Objects.toString(req.getParameter("username"), "");
        String email = Objects.toString(req.getParameter("email"), "");
        String password = Objects.toString(req.getParameter("password"), "");
        String role = Objects.toString(req.getParameter("role"), "");
        String expLvl = Objects.toString(req.getParameter("exp-lvl"), "");
        String photo = Objects.toString(req.getParameter("photo-base64"), "");

        if(photo.isEmpty() && role.equals(UserRoles.WORKER))photo = ProfilePictureGenerator.generateImage(username.charAt(0));

        User user = new User(username, email, password, role);
        Worker worker = null;
        if(role.equals("WORKER")) worker = new Worker(0, expLvl.toUpperCase(), photo);

        try
        {
            if(!dao.insertUser(user, worker)) resp.sendError(500);
            else resp.sendRedirect("/");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            resp.sendError(500);
        }
        catch (InvalidBeanException e1)
        {
            resp.sendError(400, e1.toString());
        }
    }

}
