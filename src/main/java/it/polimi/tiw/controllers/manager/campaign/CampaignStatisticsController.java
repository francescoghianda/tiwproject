package it.polimi.tiw.controllers.manager.campaign;

import it.polimi.tiw.Application;
import it.polimi.tiw.beans.Annotation;
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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/campaign/stat")
public class CampaignStatisticsController extends HttpServlet
{

    private LocationImageDao imageDao;

    public CampaignStatisticsController()
    {
        super();
    }

    @Override
    public void init()
    {
        imageDao = new LocationImageDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Campaign campaign = (Campaign) req.getAttribute("campaign");
        try
        {
            Map<LocationImage, List<Annotation>> map = imageDao.findImagesAndAnnotationsByCampaignId(campaign.getId());

            int imagesNumber = map.keySet().size();
            int totAnnotationsNumber = map.values().stream().mapToInt(Collection::size).sum();
            int avgAnnotationsPerImage = (int) map.values().stream().mapToInt(Collection::size).average().orElse(0);
            int conflictsNumber = 0;

            for(List<Annotation> annotations : map.values())
            {
                if(annotations.isEmpty())continue;
                boolean first = annotations.get(0).getValid();
                for(Annotation annotation : annotations)
                {
                    if(annotation.getValid() != first)
                    {
                        conflictsNumber++;
                        break;
                    }
                }

                //Meno efficiente (forse)
                //List<Boolean> validList = annotations.stream().map(Annotation::getValid).collect(Collectors.toList());
                //if(validList.contains(true) && validList.contains(false))conflictsNumber++;
            }

            WebContext webContext = new WebContext(req, resp, getServletContext(), req.getLocale());

            webContext.setVariable("user", req.getSession().getAttribute("user"));
            webContext.setVariable("imagesNumber", imagesNumber);
            webContext.setVariable("annotationsNumber", totAnnotationsNumber);
            webContext.setVariable("avgAnnotations", avgAnnotationsPerImage);
            webContext.setVariable("conflictsNumber", conflictsNumber);

            Application.getTemplateEngine().process("campaign-statistics", webContext, resp.getWriter());

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            resp.sendError(500);
        }
    }
}
