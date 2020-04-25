package it.polimi.tiw.controllers.manager.campaign.images;

import it.polimi.tiw.beans.Campaign;
import it.polimi.tiw.beans.LocationImage;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.dao.LocationImageDao;
import it.polimi.tiw.utils.Location;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Objects;

@WebServlet("/campaign/images/add")
@MultipartConfig(fileSizeThreshold=1024*1024*10, maxFileSize=1024*1024*50, maxRequestSize=1024*1024*100)
public class AddImageController extends HttpServlet
{
    private LocationImageDao locationImageDao;

    public AddImageController()
    {
        super();
    }

    @Override
    public void init() throws ServletException
    {
        locationImageDao = new LocationImageDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        Campaign campaign = (Campaign)req.getAttribute("campaign");

        if(!campaign.getStatus().equals("CREATED"))
        {
            resp.sendError(409, "The campaign is already started or closed.");
            return;
        }

        int campaignId = campaign.getId();

        Part imagePart = req.getPart("photo");

        byte[] imageBytes = new byte[(int)imagePart.getSize()];
        imagePart.getInputStream().read(imageBytes, 0, imageBytes.length);
        String contentType = imagePart.getHeader("content-type");
        String image = "data:"+contentType+";base64,"+Base64.getEncoder().encodeToString(imageBytes);

        String latitude = Objects.toString(req.getParameter("latitude"), "");
        String longitude = Objects.toString(req.getParameter("longitude"), "");
        String municipality = Objects.toString(req.getParameter("municipality"), "");
        String region = Objects.toString(req.getParameter("region"), "");
        String source = Objects.toString(req.getParameter("source"), "");
        String dateStr = Objects.toString(req.getParameter("date"), "");
        String resolution = Objects.toString(req.getParameter("resolution"), "");

        try
        {
            Location location = Location.fromString(latitude, longitude);
            Date date = Date.valueOf(dateStr);

            LocationImage locationImage = new LocationImage(0, campaignId, location, municipality, region, source, date, resolution, image);
            if(!locationImageDao.insertLocationImages(locationImage))resp.sendError(500);
            resp.sendRedirect("/campaign/details?id="+campaignId);
        }
        catch (NullPointerException | IllegalArgumentException e)
        {
            e.printStackTrace();
            resp.sendError(400);
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
