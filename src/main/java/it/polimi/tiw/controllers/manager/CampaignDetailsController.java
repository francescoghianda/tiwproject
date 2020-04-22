package it.polimi.tiw.controllers.manager;

import it.polimi.tiw.Application;
import it.polimi.tiw.beans.Campaign;
import it.polimi.tiw.beans.LocationImage;
import it.polimi.tiw.dao.LocationImageDao;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/campaign/details")
public class CampaignDetailsController extends HttpServlet
{
    private LocationImageDao locationImageDao;

    public CampaignDetailsController()
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
        WebContext webContext = new WebContext(req, resp, getServletContext(), req.getLocale());
        webContext.setVariable("user", req.getSession().getAttribute("user"));

        try
        {
            Campaign campaign = (Campaign) req.getAttribute("campaign");
            webContext.setVariable("campaign", campaign);
            List<LocationImage> images = locationImageDao.findImagesByCampaignId(campaign.getId());
            webContext.setVariable("images", images);
            Application.getTemplateEngine().process("campaign_details", webContext, resp.getWriter());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            resp.sendError(500);
        }
    }
}
