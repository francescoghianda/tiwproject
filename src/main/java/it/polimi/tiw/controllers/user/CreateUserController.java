package it.polimi.tiw.controllers.user;

import it.polimi.tiw.Application;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.beans.Worker;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.dao.UserDAO;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/create_user")
public class CreateUserController extends HttpServlet
{
    private UserDAO dao;

    public CreateUserController()
    {
        super();
    }

    @Override
    public void init() throws ServletException
    {
        dao = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        WebContext webContext = new WebContext(req, resp, getServletContext(), req.getLocale());
        Application.getTemplateEngine().process("create_user", webContext, resp.getWriter());
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

        User user = new User(username, email, password, role);
        Worker worker = null;
        if(role.equals("WORKER")) worker = new Worker(0, expLvl.toUpperCase(), photo);

        try
        {
            if(worker == null)dao.insertManager(user);
            else dao.insertWorker(user, worker);
            resp.sendRedirect("/");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            resp.sendError(500);
        }
        catch (InvalidBeanException e1)
        {
            resp.sendError(400);
        }
    }

}
