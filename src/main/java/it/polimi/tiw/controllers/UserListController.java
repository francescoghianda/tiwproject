package it.polimi.tiw.controllers;

import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.UserDAO;
import it.polimi.tiw.utils.DBConnectionFactory;
import it.polimi.tiw.utils.TemplateEngineFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/allusers")
public class UserListController extends HttpServlet
{
    private Connection dbConnection;
    private TemplateEngine templateEngine;
    private UserDAO userDAO;

    private boolean isDAOsInitialized;

    private final String USERS_PAGE_PATH = "thtest.html";

    public UserListController()
    {
        super();
    }


    @Override
    public void init() throws ServletException
    {
        dbConnection = DBConnectionFactory.getConnection(getServletContext());
        templateEngine = TemplateEngineFactory.getTemplateEngine(getServletContext(), TemplateMode.HTML);

        userDAO = new UserDAO(dbConnection);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        try
        {
            List<User> users = userDAO.getAllUsers();

            WebContext webContext = new WebContext(req, resp, getServletContext());
            webContext.setVariable("users", users);
            templateEngine.process(USERS_PAGE_PATH, webContext, resp.getWriter());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            resp.sendError(500, "Database connection error!");
        }
    }

}
