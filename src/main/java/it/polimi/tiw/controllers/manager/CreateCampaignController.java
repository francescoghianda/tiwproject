package it.polimi.tiw.controllers.manager;

import it.polimi.tiw.beans.Campaign;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.dao.CampaignDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/create_campaign")
public class CreateCampaignController extends HttpServlet
{
    private CampaignDao campaignDao;

    public CreateCampaignController()
    {
        super();
    }

    @Override
    public void init()
    {
        campaignDao = new CampaignDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        String campaignName = Objects.toString(req.getParameter("campaign-name"), "");
        String client = Objects.toString(req.getParameter("campaign-client"), "");
        int managerId = ((User)req.getSession().getAttribute("user")).getId();

        Campaign campaign = new Campaign(0, managerId, campaignName, client, "CREATED");
        try
        {
            if(!campaignDao.insertCampaign(campaign))resp.sendError(500);
            else resp.sendRedirect("/");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            resp.sendError(500);
        }
        catch (InvalidBeanException e)
        {
            resp.sendError(400, e.toString());
        }
    }
}
