package it.polimi.tiw.filters;

import it.polimi.tiw.beans.User;
import it.polimi.tiw.beans.Worker;
import it.polimi.tiw.dao.UserDao;
import it.polimi.tiw.utils.beans.UserRoles;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebFilter(filterName = "WorkerFilter")
public class WorkerFilter implements Filter
{
    private UserDao userDao;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        userDao = new UserDao();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        User user = (User) request.getSession().getAttribute("user");
        if(!user.getRole().equals(UserRoles.WORKER)) response.sendError(401);
        else
        {
            Worker cached = (Worker) request.getSession().getAttribute("cached-worker");

            if(cached == null)
            {
                try
                {
                    Optional<Worker> worker = userDao.findWorkerByUserId(user.getId());
                    if(!worker.isPresent())
                    {
                        request.getSession().invalidate();
                        response.sendError(500);
                        return;
                    }
                    cached = worker.get();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    response.sendError(500);
                    return;
                }

                request.getSession().setAttribute("cached-worker", cached);
            }

            request.setAttribute("worker", cached);

            chain.doFilter(request, servletResponse);
        }
    }
}
