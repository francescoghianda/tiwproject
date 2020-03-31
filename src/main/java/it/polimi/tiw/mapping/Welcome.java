package it.polimi.tiw.mapping;

import it.polimi.tiw.utils.TemplateEngineFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("")
public class Welcome extends HttpServlet
{
    private TemplateEngine templateEngine;

    public Welcome()
    {
        super();
    }

    @Override
    public void init() throws ServletException
    {
        templateEngine = TemplateEngineFactory.getTemplateEngine(getServletContext(), TemplateMode.HTML);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        HttpSession session = req.getSession(false);
        WebContext webContext = new WebContext(req, resp, getServletContext());
        webContext.setVariable("user", session.getAttribute("user"));
        templateEngine.process("index.html", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }
}
