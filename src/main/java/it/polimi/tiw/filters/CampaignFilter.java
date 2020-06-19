package it.polimi.tiw.filters;

import it.polimi.tiw.beans.Campaign;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.CampaignDao;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

@WebFilter(filterName = "ManagerCampaignFilter")
@MultipartConfig
public class CampaignFilter extends HttpFilter
{
    private CampaignDao campaignDao;

    @Override
    public void init()
    {
        campaignDao = new CampaignDao();
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        int managerId = ((User)request.getSession().getAttribute("user")).getId();
        String campaignIdStr = Objects.toString(request.getParameter("id"), "");

        try
        {
            int campaignId = Integer.parseInt(campaignIdStr);

            Campaign cachedCampaign = (Campaign) request.getSession().getAttribute("cached-campaign");

            Optional<Campaign> campaign = (cachedCampaign != null && cachedCampaign.getId() == campaignId) ? Optional.of(cachedCampaign) : campaignDao.findCampaignById(campaignId);

            if(!campaign.isPresent())
            {
                response.sendError(404);
                return;
            }
            else if(campaign.get().getManagerId() != managerId)
            {
                response.sendError(401);
                return;
            }

            request.getSession().setAttribute("cached-campaign", campaign.get());
            request.setAttribute("campaign", campaign.get());
            filterChain.doFilter(request, response);


        }
        catch (NumberFormatException e)
        {
            response.sendError(400);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            response.sendError(500);
        }
    }
}
