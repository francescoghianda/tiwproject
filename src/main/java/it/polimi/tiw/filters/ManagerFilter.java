package it.polimi.tiw.filters;

import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.UserDao;
import it.polimi.tiw.utils.beans.UserRoles;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "ManagerFilter")
public class ManagerFilter implements Filter
{

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        User user = (User) request.getSession().getAttribute("user");
        if(!user.getRole().equals(UserRoles.MANAGER)) response.sendError(401);
        else chain.doFilter(request, servletResponse);
    }
}
