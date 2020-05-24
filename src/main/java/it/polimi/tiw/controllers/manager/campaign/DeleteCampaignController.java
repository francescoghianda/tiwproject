package it.polimi.tiw.controllers.manager.campaign;

import it.polimi.tiw.beans.Campaign;
import it.polimi.tiw.dao.CampaignDao;
import it.polimi.tiw.utils.beans.CampaignStatus;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/campaign/delete")
public class DeleteCampaignController extends HttpServlet
{

    private CampaignDao campaignDao;

    public DeleteCampaignController()
    {
        super();
    }

    @Override
    public void init(ServletConfig config)
    {
        campaignDao = new CampaignDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        Campaign campaign = (Campaign)req.getAttribute("campaign");

        if(campaign.getStatus().equals(CampaignStatus.STARTED))
        {
            resp.sendError(409, "Cannot delete campaign with status <STARTED>.");
            return;
        }

        try
        {
            if(!campaignDao.deleteCampaign(campaign.getId()))
            {
                resp.sendError(500, "Somethings goes wrong.");
                return;
            }

            req.getSession().removeAttribute("cached-campaign");
            resp.setStatus(200);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            resp.sendError(500, "SQL error.");
        }
    }
}
