package it.polimi.tiw.controllers.errors;

import it.polimi.tiw.Application;
import org.thymeleaf.context.WebContext;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebServlet("/error")
public class ErrorHandler extends HttpServlet
{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        if(request.getDispatcherType() != DispatcherType.ERROR)
        {
            response.sendRedirect("/");
            return;
        }

        String errorCode = String.valueOf(response.getStatus());
        String errorMessage = Objects.toString(request.getAttribute("javax.servlet.error.message"), "");

        WebContext webContext = new WebContext(request, response, getServletContext(), request.getLocale());
        webContext.setVariable("errorCode", errorCode);
        webContext.setVariable("errorMessage", errorMessage);

        Application.getTemplateEngine().process("error/error", webContext, response.getWriter());
    }
}
