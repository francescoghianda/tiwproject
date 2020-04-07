package it.polimi.tiw.utils;

import it.polimi.tiw.Application;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.ServletContextTemplateResource;

import javax.servlet.ServletContext;
import java.util.Locale;
import java.util.Map;

public class PathMessageResolver extends StandardMessageResolver
{
    private ServletContext context;
    private String path;

    public PathMessageResolver(ServletContext context)
    {
        this.context = context;
        path = context.getInitParameter("i18n");
        if(path != null && !path.endsWith("/"))path += "/";
    }

    public PathMessageResolver(ServletContext context, String path)
    {
        this.context = context;
        this.path = path;
    }

    @Override
    protected Map<String, String> resolveMessagesForTemplate(String template, ITemplateResource templateResource, Locale locale)
    {
        if(path == null)super.resolveMessagesForTemplate(template, templateResource, locale);
        String messagesPath = path+template+Application.getTemplateResolver().getSuffix();
        return super.resolveMessagesForTemplate(messagesPath, new ServletContextTemplateResource(context, messagesPath, "UTF-8"), locale);
    }
}
