package it.polimi.tiw;

import it.polimi.tiw.utils.DBConnectionFactory;
import it.polimi.tiw.utils.i18n.PathMessageResolver;
import it.polimi.tiw.utils.sql.ConnectionManager;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import java.sql.Connection;

public class Application
{
    private static boolean initialized;

    private static String templatesPath = "/WEB-INF/templates/";
    private static TemplateEngine templateEngine;
    private static ServletContextTemplateResolver templateResolver;
    private static DBConnectionFactory connectionFactory;

    private Application() {}

    public static void init(ServletContext servletContext)
    {
        String templatesPathParam = servletContext.getInitParameter("templatesPath");
        if(templatesPathParam != null)templatesPath = templatesPathParam;

        templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setCacheable(false);
        templateResolver.setPrefix(templatesPath);
        templateResolver.setSuffix(".html");
        templateEngine = new TemplateEngine();
        templateEngine.setMessageResolver(new PathMessageResolver(servletContext));
        templateEngine.setTemplateResolver(templateResolver);

        try
        {
            ConnectionManager.newInstance(servletContext);
            connectionFactory = new DBConnectionFactory(servletContext);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        initialized = true;
    }

    public static String getTemplatesPath()
    {
        return templatesPath;
    }

    public static TemplateEngine getTemplateEngine()
    {
        checkInitialization();
        return templateEngine;
    }

    public static ServletContextTemplateResolver getTemplateResolver()
    {
        checkInitialization();
        return templateResolver;
    }

    @Deprecated
    public static Connection getDBConnection() throws UnavailableException
    {
        checkInitialization();
        if(connectionFactory == null) throw  new UnavailableException("JDBC driver not found!");

        return connectionFactory.getConnection();
    }

    private static void checkInitialization()
    {
        if(!initialized)throw new IllegalStateException("Application must be initialized!");
    }

}
