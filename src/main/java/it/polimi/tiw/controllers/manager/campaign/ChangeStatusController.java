package it.polimi.tiw.controllers.manager.campaign;

import it.polimi.tiw.beans.Campaign;
import it.polimi.tiw.dao.CampaignDao;
import it.polimi.tiw.dao.LocationImageDao;
import it.polimi.tiw.utils.beans.CampaignStatus;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/campaign/change-status")
public class ChangeStatusController extends HttpServlet
{
    private CampaignDao campaignDao;
    private LocationImageDao locationImageDao;

    public ChangeStatusController()
    {
        super();
    }

    @Override
    public void init()
    {
        campaignDao = new CampaignDao();
        locationImageDao = new LocationImageDao();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        Campaign campaign = (Campaign)req.getAttribute("campaign");
        String newStatus = req.getParameter("status");
        String currentStatus = campaign.getStatus();

        if(!CampaignStatus.isValid(newStatus))
        {
            resp.sendError(400, "Invalid status ("+newStatus+").");
            return;
        }

        if(currentStatus.equals(CampaignStatus.CLOSED))
        {
            resp.sendError(409, "Cannot change status of a closed campaign.");
            return;
        }
        else if(currentStatus.equals(CampaignStatus.CREATED) && !newStatus.equals(CampaignStatus.STARTED) ||
                currentStatus.equals(CampaignStatus.STARTED) && !newStatus.equals(CampaignStatus.CLOSED))
        {
            resp.sendError(409, "Cannot change the status to "+newStatus+" of a campaign with status "+currentStatus+".");
            return;
        }

        try
        {
            if(currentStatus.equals(CampaignStatus.STARTED))
            {
                if(campaignDao.updateCampaignStatus(campaign.getId(), CampaignStatus.CLOSED)) resp.sendRedirect("/campaign/details?id="+campaign.getId());
                else resp.sendError(500, "An error occurred while changing campaign status.");
                return;
            }

            //from this point the currentStatus is always CREATED.
            if(!locationImageDao.atLeastOneByCampaignId(campaign.getId()))
            {
                resp.sendError(409, "Cannot change the status to STARTED of an empty campaign.");
                return;
            }

            if(campaignDao.updateCampaignStatus(campaign.getId(), CampaignStatus.STARTED)) resp.sendRedirect("/campaign/details?id="+campaign.getId());
            else resp.sendError(500, "An error occurred while changing campaign status.");

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            resp.sendError(500, "A SQL error occurred while changing campaign status.");
        }
    }
}
