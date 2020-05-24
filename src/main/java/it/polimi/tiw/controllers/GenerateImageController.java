package it.polimi.tiw.controllers;

import it.polimi.tiw.beans.User;
import it.polimi.tiw.utils.ProfilePictureGenerator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/generate-user-picture")
public class GenerateImageController extends HttpServlet
{

    public GenerateImageController()
    {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        User user = (User) req.getSession().getAttribute("user");
        PrintWriter pw = resp.getWriter();
        pw.print(ProfilePictureGenerator.generateImage(user.getUsername().charAt(0)));
        pw.flush();
        resp.setStatus(200);
    }
}
