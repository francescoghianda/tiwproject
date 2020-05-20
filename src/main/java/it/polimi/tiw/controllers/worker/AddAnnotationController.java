package it.polimi.tiw.controllers.worker;

import it.polimi.tiw.Application;
import it.polimi.tiw.beans.*;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.dao.AnnotationDao;
import it.polimi.tiw.dao.CampaignDao;
import it.polimi.tiw.dao.LocationImageDao;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/add-annotation")
public class AddAnnotationController extends HttpServlet
{
    private CampaignDao campaignDao;
    private AnnotationDao annotationDao;
    private LocationImageDao locationImageDao;

    public AddAnnotationController()
    {
        super();
    }

    @Override
    public void init()
    {
        campaignDao = new CampaignDao();
        annotationDao = new AnnotationDao();
        locationImageDao = new LocationImageDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        User user = (User) req.getSession().getAttribute("user");
        try
        {
            Optional<Campaign> campaign = check(req, resp);
            if(!campaign.isPresent())return;

            List<LocationImage> images = locationImageDao.findImagesByCampaignId(campaign.get().getId(), false);
            List<Annotation> annotations = annotationDao.findAnnotationByUserId(user.getId());
            Map<LocationImage, Optional<Annotation>> imageMap = buildImageAnnotationMap(images, annotations);

            WebContext webContext = new WebContext(req, resp, getServletContext(), req.getLocale());
            webContext.setVariable("worker", (Worker) req.getAttribute("worker"));
            webContext.setVariable("user", user);
            webContext.setVariable("campaign", campaign.get());
            webContext.setVariable("imageMap", imageMap);
            webContext.setVariable("hasImage", !imageMap.isEmpty());
            Application.getTemplateEngine().process("add-annotation", webContext, resp.getWriter());

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            resp.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        User user = (User) req.getSession().getAttribute("user");

        try
        {
            Optional<Campaign> campaign = check(req, resp);
            if(!campaign.isPresent())return;

            int imageId = Integer.parseInt(req.getParameter("image-id"));
            Date currentDate = new Date(new java.util.Date().getTime());
            boolean valid = Boolean.parseBoolean(req.getParameter("valid"));
            String trust = req.getParameter("trust");
            String notes = req.getParameter("notes");

            Annotation annotation = new Annotation(0, user.getId(), imageId, currentDate, valid, trust, notes);
            if(!annotationDao.insertAnnotation(annotation))
            {
                resp.sendError(409);
                return;
            }

            resp.sendRedirect("/add-annotation?id="+campaign.get().getId());
        }
        catch (NumberFormatException e)
        {
            resp.sendError(400);
        }
        catch (InvalidBeanException e)
        {
            resp.sendError(400, e.toString());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            resp.sendError(500);
        }
    }

    private Map<LocationImage, Optional<Annotation>> buildImageAnnotationMap(List<LocationImage> images, List<Annotation> annotations)
    {
        Map<LocationImage, Optional<Annotation>> map = new HashMap<>();

        for(LocationImage image : images)
        {
            boolean found = false;
            for(Annotation annotation : annotations)
            {
                if(annotation.getImageId() == image.getId())
                {
                    map.put(image, Optional.of(annotation));
                    found = true;
                    break;
                }
            }
            if(!found)map.put(image, Optional.empty());
        }
        return map;
    }

    private Optional<Campaign> check(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException
    {
        try
        {
            User user = (User) req.getSession().getAttribute("user");

            int campaignId = Integer.parseInt(Objects.toString(req.getParameter("id"), ""));

            Optional<Campaign> campaign = campaignDao.findCampaignById(campaignId);
            if(!campaign.isPresent())
            {
                resp.sendError(400, "This campaign does not exist.");
                return Optional.empty();
            }

            if(!campaignDao.existSubscription(campaignId, user.getId()))
            {
                resp.sendError(401, "You are not subscribed to this campaign.");
                return Optional.empty();
            }

            return campaign;
        }
        catch (NumberFormatException e)
        {
            resp.sendError(400);
            return Optional.empty();
        }
    }
}
