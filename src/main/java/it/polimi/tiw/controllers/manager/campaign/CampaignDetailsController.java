package it.polimi.tiw.controllers.manager.campaign;

import it.polimi.tiw.Application;
import it.polimi.tiw.beans.*;
import it.polimi.tiw.dao.AnnotationDao;
import it.polimi.tiw.dao.LocationImageDao;
import it.polimi.tiw.utils.beans.CampaignStatus;
import it.polimi.tiw.utils.beans.JoinedBean;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@WebServlet("/campaign/details")
public class CampaignDetailsController extends HttpServlet
{
    private LocationImageDao locationImageDao;
    private AnnotationDao annotationDao;

    public CampaignDetailsController()
    {
        super();
    }

    @Override
    public void init() throws ServletException
    {
        locationImageDao = new LocationImageDao();
        annotationDao = new AnnotationDao();
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
            webContext.setVariable("created", campaign.getStatus().equals(CampaignStatus.CREATED));
            List<LocationImage> images = locationImageDao.findImagesByCampaignId(campaign.getId(), false);
            boolean hasImage = locationImageDao.atLeastOneByCampaignId(campaign.getId());
            webContext.setVariable("hasImage", hasImage);
            webContext.setVariable("images", images);

            HashMap<Integer, List<JoinedBean<Annotation, User, Worker>>> annotations = new HashMap<>();

            for(LocationImage image : images)
                annotations.put(image.getId(), annotationDao.findAnnotationAndUserByImageId(image.getId()));

            webContext.setVariable("annotations", annotations);

            Application.getTemplateEngine().process("campaign-details", webContext, resp.getWriter());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            resp.sendError(500);
        }
    }
}
