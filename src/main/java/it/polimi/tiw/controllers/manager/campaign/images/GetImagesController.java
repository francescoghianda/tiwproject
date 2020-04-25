package it.polimi.tiw.controllers.manager.campaign.images;

import it.polimi.tiw.beans.Campaign;
import it.polimi.tiw.beans.LocationImage;
import it.polimi.tiw.dao.AnnotationDao;
import it.polimi.tiw.dao.LocationImageDao;
import it.polimi.tiw.utils.LocationImageJsonBuilder;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@WebServlet("/campaign/images")
public class GetImagesController extends HttpServlet
{
    private AnnotationDao annotationDao;
    private LocationImageDao locationImageDao;

    public GetImagesController()
    {
        super();
    }

    @Override
    public void init()
    {
        annotationDao = new AnnotationDao();
        locationImageDao = new LocationImageDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        Campaign campaign = (Campaign) req.getAttribute("campaign");
        boolean media = Boolean.parseBoolean(Objects.toString(req.getParameter("media"), "true"));

        try(JsonWriter writer = Json.createWriter(resp.getWriter()))
        {
            List<LocationImage> locationImageList = locationImageDao.findImagesByCampaignId(campaign.getId(), media);
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("campaignStatus", campaign.getStatus());
            JsonArrayBuilder imageArray = Json.createArrayBuilder();
            for (LocationImage locationImage : locationImageList)
            {
                LocationImageJsonBuilder locationImageJsonBuilder = new LocationImageJsonBuilder(locationImage);
                locationImageJsonBuilder.retrieveData(annotationDao);
                imageArray.add(locationImageJsonBuilder.getJsonBuilder());
            }
            objectBuilder.add("images", imageArray);
            writer.writeObject(objectBuilder.build());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            resp.sendError(500);
        }
    }
}
