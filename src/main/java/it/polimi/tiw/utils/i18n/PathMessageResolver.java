package it.polimi.tiw.utils.i18n;

import it.polimi.tiw.Application;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.ServletContextTemplateResource;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PathMessageResolver extends StandardMessageResolver
{
    private ServletContext context;
    private String path;
    private String globalMessagesPath;

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
        globalMessagesPath = path+"global"+Application.getTemplateResolver().getSuffix();

        Map<String, String> globalMessages = super.resolveMessagesForTemplate(template, new ServletContextTemplateResource(context, globalMessagesPath, "UTF-8"), locale);
        Map<String, String> messages = globalMessages != null ? new HashMap<>(globalMessages) : new HashMap<>();
        Map<String, String> localMessages;

        if(path == null) localMessages = super.resolveMessagesForTemplate(template, templateResource, locale);
        else
        {
            String messagesPath = path+template+Application.getTemplateResolver().getSuffix();
            localMessages = super.resolveMessagesForTemplate(messagesPath, new ServletContextTemplateResource(context, messagesPath, "UTF-8"), locale);
        }

        if(localMessages != null)messages.putAll(localMessages);
        return messages;
    }
}
