package it.polimi.tiw.controllers;

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

@WebServlet("/validate-data")
public class ValidateDataController extends HttpServlet
{
    private UserDao userDao;
    private CampaignDao campaignDao;

    public ValidateDataController()
    {
        super();
    }

    @Override
    public void init() throws ServletException
    {
        userDao = new UserDao();
        campaignDao = new CampaignDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String campaignName = req.getParameter("campaign-name");

        try(JsonGenerator generator = Json.createGenerator(resp.getWriter()))
        {
            generator.writeStartObject();
            if(username != null) generator.write("valid-username", !userDao.usernameExist(username) && !username.isEmpty());
            if(email != null) generator.write("valid-email", !userDao.emailExist(email) && !email.isEmpty());
            if(campaignName != null) generator.write("valid-campaign-name", !campaignDao.campaignNameExist(campaignName) && !campaignName.isEmpty());
            generator.writeEnd();
        }
        catch (SQLException e)
        {
            resp.sendError(500);
            e.printStackTrace();
        }
    }
}
