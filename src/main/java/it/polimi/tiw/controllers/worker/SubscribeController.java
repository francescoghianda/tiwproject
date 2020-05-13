package it.polimi.tiw.controllers.worker;

import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.CampaignDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/subscribe")
public class SubscribeController extends HttpServlet
{
    public CampaignDao campaignDao;

    public SubscribeController()
    {
        super();
    }

    @Override
    public void init()
    {
        campaignDao = new CampaignDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        User user = (User) req.getSession().getAttribute("user");

        try
        {
            int campaignId = Integer.parseInt(Objects.toString(req.getParameter("id"), ""));
            if(campaignId < 1) throw new IllegalArgumentException();
            if(campaignDao.addSubscription(campaignId, user.getId())) resp.sendRedirect("/add-annotation?id="+campaignId);
            else resp.sendError(409, "You are already subscribed to this campaign.");
        }
        catch (IllegalArgumentException e)
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
