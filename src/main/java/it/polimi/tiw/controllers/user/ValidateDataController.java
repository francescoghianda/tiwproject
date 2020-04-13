package it.polimi.tiw.controllers.user;

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

@WebServlet("/validate_data")
public class ValidateDataController extends HttpServlet
{
    private UserDao dao;

    public ValidateDataController()
    {
        super();
    }

    @Override
    public void init() throws ServletException
    {
        dao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        String username = req.getParameter("username");
        String email = req.getParameter("email");

        try(JsonGenerator generator = Json.createGenerator(resp.getWriter()))
        {
            generator.writeStartObject();
            if(username != null) generator.write("valid-username", !dao.usernameExist(username) && !username.isEmpty());
            if(email != null) generator.write("valid-email", !dao.emailExist(email) && !email.isEmpty());
            generator.writeEnd();
        }
        catch (SQLException e)
        {
            resp.sendError(500);
            e.printStackTrace();
        }
    }
}
