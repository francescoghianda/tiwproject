package it.polimi.tiw.controllers;

import it.polimi.tiw.Application;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.UserDao;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/settings")
public class SettingsController extends HttpServlet
{
    private UserDao userDAO;

    @Override
    public void init() throws ServletException
    {
        userDAO = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        HttpSession session = req.getSession();

        WebContext webContext = new WebContext(req, resp, getServletContext(), req.getLocale());

        User user = (User) session.getAttribute("user");
        if(user == null)throw new ServletException();

        try
        {
            webContext.setVariable("user", user);
            webContext.setVariable("worker", userDAO.findWorkerByUserId(user.getId()).orElse(null));

            Application.getTemplateEngine().process("settings", webContext, resp.getWriter());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new UnavailableException("Database connection error!");
        }
    }
}
