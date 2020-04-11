package it.polimi.tiw.listener;

import it.polimi.tiw.Application;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener
{
    @Override
    public void contextInitialized(ServletContextEvent event)
    {
        Application.init(event.getServletContext());
    }
}
