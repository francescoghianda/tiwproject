package it.polimi.tiw.filters;

import it.polimi.tiw.authentication.AuthenticationHelper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(filterName = "AuthenticationFilter")
public class AuthenticationFilter extends HttpFilter
{
    private List<String> exclusions;

    @Override
    public void init()
    {
        String exclusionsParameter = getServletContext().getInitParameter("loginFilterExclusions");
        exclusions = Arrays.asList(exclusionsParameter.split(","));
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        String requestURI = request.getRequestURI();

        if(exclusions.stream().anyMatch(requestURI::startsWith))
        {
            filterChain.doFilter(request, response);
            return;
        }

        if(!AuthenticationHelper.isAuthenticated(request.getSession(false)))
        {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login");
            String query = request.getQueryString();
            query = query == null ? "" : "?"+query;
            request.setAttribute("redirect", requestURI+query);
            dispatcher.forward(request, response);
        }
        else filterChain.doFilter(request, response);
    }
}
