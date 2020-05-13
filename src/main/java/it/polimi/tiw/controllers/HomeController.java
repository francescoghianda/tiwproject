package it.polimi.tiw.controllers;

import it.polimi.tiw.Application;
import it.polimi.tiw.beans.Campaign;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.beans.Worker;
import it.polimi.tiw.dao.CampaignDao;
import it.polimi.tiw.dao.UserDao;
import it.polimi.tiw.utils.beans.CampaignStatus;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@WebServlet("")
public class HomeController extends HttpServlet
{
    private UserDao userDAO;
    private CampaignDao campaignDao;

    public HomeController()
    {
        super();
    }

    @Override
    public void init() throws ServletException
    {
        userDAO = new UserDao();
        campaignDao = new CampaignDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        HttpSession session = req.getSession();

        WebContext webContext = new WebContext(req, resp, getServletContext(), req.getLocale());

        User user = (User) session.getAttribute("user");
        if(user == null)throw new ServletException();

        try
        {
            webContext.setVariable("user", user);
            String template;
            if(user.getRole().equals("WORKER"))
            {
                template = "worker-home";

                List<Campaign> freeCampaigns = campaignDao.findCampaignByStatus(CampaignStatus.STARTED);
                List<Campaign> subCampaigns = new ArrayList<>();
                List<Integer> subscription = campaignDao.findSubscription(user.getId());

                Iterator<Campaign> iterator = freeCampaigns.iterator();
                while (iterator.hasNext())
                {
                    Campaign campaign = iterator.next();
                    if(subscription.contains(campaign.getId()))
                    {
                        subCampaigns.add(campaign);
                        iterator.remove();
                    }
                }

                webContext.setVariable("freeCampaigns", freeCampaigns);
                webContext.setVariable("subCampaigns", subCampaigns);
                webContext.setVariable("worker", (Worker) userDAO.findWorkerByUserId(user.getId()).orElse(null));
            }
            else
            {
                template = "manager-home";
                webContext.setVariable("campaigns", (List<Campaign>) campaignDao.findCampaignByManagerId(user.getId()));
            }


            Application.getTemplateEngine().process(template, webContext, resp.getWriter());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new UnavailableException("Database connection error!");
        }
    }
}
