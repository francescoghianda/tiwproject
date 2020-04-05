package it.polimi.tiw.controllers.user;

import it.polimi.tiw.Application;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.beans.Worker;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.dao.UserDAO;
import it.polimi.tiw.utils.JsonUtils;
import org.thymeleaf.context.WebContext;

import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/create-user")
public class CreateUserController extends HttpServlet
{
    private Connection connection;
    private UserDAO dao;

    public CreateUserController()
    {
        super();
    }

    @Override
    public void init() throws ServletException
    {
        connection = Application.getDBConnection();
        dao = new UserDAO(connection);
    }

    @Override
    public void destroy()
    {
        try
        {
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
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
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User.Role role = User.Role.valueOf(req.getParameter("role"));
        String expLvl = req.getParameter("exp-lvl");
        String photo = req.getParameter("photo-base64");

        User user = new User(username, email, password, role);
        Worker worker = null;
        if(role == User.Role.WORKER) worker = new Worker(0, expLvl.toUpperCase(), photo);

        try
        {
            if(worker == null)dao.insertManager(user);
            else dao.insertWorker(user, worker);
        }
        catch (SQLException | InvalidBeanException e)
        {
            e.printStackTrace();

            resp.sendRedirect("/create-user");
            return;

        }

        resp.sendRedirect("/");
    }

}
