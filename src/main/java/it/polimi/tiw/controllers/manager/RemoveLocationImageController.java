package it.polimi.tiw.controllers.manager;

import it.polimi.tiw.dao.LocationImageDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/campaign/remove_image")
public class RemoveLocationImageController extends HttpServlet
{
    private LocationImageDao locationImageDao;

    public RemoveLocationImageController()
    {
        super();
    }

    @Override
    public void init() throws ServletException
    {
        locationImageDao = new LocationImageDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        try
        {
            int imageId = Integer.parseInt(req.getParameter("image_id"));
            locationImageDao.deleteLocationImage(imageId);
            resp.setStatus(200);
        }
        catch (NumberFormatException e)
        {
            resp.sendError(400);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            resp.sendError(500);
        }

    }
}
