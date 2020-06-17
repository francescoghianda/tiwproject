package it.polimi.tiw.controllers.user;

import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.UserDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/edit-photo")
public class EditUserPhotoController extends HttpServlet
{
    private UserDao userDao;

    public EditUserPhotoController()
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
        User user = (User) req.getSession().getAttribute("user");
        String photo = req.getParameter("photo-base64");

        try
        {
            if(!userDao.updateUserPhoto(user.getId(), photo))
            {
                resp.sendError(400, "Invalid image.");
                return;
            }
            resp.setStatus(200);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            resp.sendError(500);
        }
    }
}
