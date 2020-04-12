package it.polimi.tiw.controllers;

import it.polimi.tiw.beans.LocationImage;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.dao.LocationImageDAO;
import it.polimi.tiw.utils.Location;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@WebServlet("/test")
public class TestingServlet extends HttpServlet
{
    private LocationImageDAO imageDAO;

    @Override
    public void init() throws ServletException
    {
        imageDAO = new LocationImageDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        /*try
        {
            List<LocationImage> images = new ArrayList<>();
            LocationImage[] array = new LocationImage[5];
            for(int i = 0; i < 5; i++)
            {
                LocationImage image = new LocationImage();
                image.setResolution("GOOD");
                image.setDate(new Date(new java.util.Date().getTime()));
                image.setSource("A source");
                image.setRegion("A region");
                image.setMunicipality("AAAA");
                image.setId(0);
                image.setCampaignId(1);
                image.setLocation(Location.fromWKT("POINT(9.245673 45.455902)"));
                images.add(image);
            }
            boolean committed = imageDAO.insertImages(images.toArray(array));
            System.out.println(committed);
        }
        catch (SQLException | InvalidBeanException e)
        {
            e.printStackTrace();
        }*/
    }
}
