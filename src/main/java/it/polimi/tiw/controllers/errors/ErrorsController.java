package it.polimi.tiw.controllers.errors;

import it.polimi.tiw.Application;
import org.thymeleaf.context.WebContext;

import javax.servlet.DispatcherType;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/error")
public class ErrorsController extends HttpServlet
{

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        if(request.getDispatcherType() != DispatcherType.ERROR)
        {
            response.sendRedirect("/");
            return;
        }

        String errorCode = String.valueOf(response.getStatus());

        WebContext webContext = new WebContext(request, response, getServletContext(), request.getLocale());
        webContext.setVariable("errorCode", errorCode);

        Application.getTemplateEngine().process("error/error", webContext, response.getWriter());
    }
}
