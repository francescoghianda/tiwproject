package it.polimi.tiw.controllers.api.v1;

import it.polimi.tiw.beans.Campaign;
import it.polimi.tiw.beans.LocationImage;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.CampaignDao;
import it.polimi.tiw.dao.LocationImageDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

@WebServlet("/get-image")
public class GetImageController extends HttpServlet
{
    private CampaignDao campaignDao;
    private LocationImageDao locationImageDao;

    public GetImageController()
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        User user = (User) req.getSession().getAttribute("user");

        try
        {
            String a = Objects.toString(req.getParameter("id"), "");
            int imageIdStr = Integer.parseInt(a);
            Optional<LocationImage> locationImage = locationImageDao.findLocationImageById(imageIdStr);

            if(!locationImage.isPresent())
            {
                resp.sendError(404);
                return;
            }

            Optional<Campaign> campaign = campaignDao.findCampaignById(locationImage.get().getCampaignId());
            if(!campaign.isPresent())throw new ServletException();


            if(user.getRole().equals("MANAGER"))
            {
                if(campaign.get().getManagerId() != user.getId())
                {
                    resp.sendError(401, "You are not the ownership of this campaign");
                    return;
                }
            }
            else if(!campaignDao.existSubscription(campaign.get().getId(), user.getId()))
            {
                resp.sendError(401, "You are not subscribed to this campaign");
                return;
            }

            PrintWriter writer = resp.getWriter();
            writer.print(locationImage.get().getImage());
            writer.flush();

        }
        catch (NumberFormatException e)
        {
            resp.sendError(400);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            resp.sendError(500);
        }

    }
}
