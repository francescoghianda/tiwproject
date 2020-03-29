package it.polimi.tiw.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/forward/*")
public class Filter1 implements Filter
{


    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        ServletOutputStream out = response.getOutputStream();
        out.println("Filter");


        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy()
    {

    }
}
