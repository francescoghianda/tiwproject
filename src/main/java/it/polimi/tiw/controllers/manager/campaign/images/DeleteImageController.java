package it.polimi.tiw.controllers.manager.campaign.images;

import it.polimi.tiw.beans.Campaign;
import it.polimi.tiw.dao.LocationImageDao;
import it.polimi.tiw.utils.beans.CampaignStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/campaign/images/delete")
public class DeleteImageController extends HttpServlet
{
    private LocationImageDao locationImageDao;

    public DeleteImageController()
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
        Campaign campaign = (Campaign)req.getAttribute("campaign");
        if(!campaign.getStatus().equals(CampaignStatus.CREATED))
        {
            resp.sendError(409, "Cannot modify a started or closed campaign.");
            return;
        }

        try
        {
            int imageId = Integer.parseInt(req.getParameter("image-id"));
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
