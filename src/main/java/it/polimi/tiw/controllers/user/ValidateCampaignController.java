package it.polimi.tiw.controllers.user;

import it.polimi.tiw.dao.CampaignDao;
import it.polimi.tiw.dao.UserDao;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/validate_campaign")
public class ValidateCampaignController extends HttpServlet
{
    private CampaignDao dao;

    public ValidateCampaignController()
    {
        super();
    }

    @Override
    public void init() throws ServletException
    {
        dao = new CampaignDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        String campaignName = req.getParameter("campaignName");

        try(JsonGenerator generator = Json.createGenerator(resp.getWriter()))
        {
            generator.writeStartObject(); ///inizi a scrivere l'oggeto json
            if(campaignName != null) generator.write("valid-campaign-name", !dao.campaignNameExist(campaignName) && !campaignName.isEmpty());
            generator.writeEnd();
        }
        catch (SQLException e)
        {
            resp.sendError(500);
            e.printStackTrace();
        }
    }
}
