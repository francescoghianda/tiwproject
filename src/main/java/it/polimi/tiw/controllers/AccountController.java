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

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/account")
public class AccountController extends HttpServlet
{

    private UserDao userDao;

    public AccountController()
    {
        super();
    }

    @Override
    public void init()
    {
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        User user = (User) req.getSession().getAttribute("user");

        try
        {
            WebContext webContext = new WebContext(req, resp, getServletContext(), req.getLocale());
            webContext.setVariable("user", user);
            webContext.setVariable("worker", userDao.findWorkerByUserId(user.getId()).orElse(null));
            Application.getTemplateEngine().process("account", webContext, resp.getWriter());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new UnavailableException("Database connection error!");
        }
    }
}
