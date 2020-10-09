package ru.otus.webserver.web.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdminAuthorizationFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String uri = httpServletRequest.getRequestURI();

        HttpSession session = httpServletRequest.getSession(false);

        if (session != null) {
            if (uri.endsWith(AdminServlet.ENDPOINT)) {
                boolean isAdmin = Boolean.parseBoolean(String.valueOf(session.getAttribute("isAdmin")));
                if (!isAdmin) {
                    httpServletResponse.sendRedirect(LoginServlet.ENDPOINT);
                } else {
                    chain.doFilter(httpServletRequest, httpServletResponse);
                }
            }
        } else {
            httpServletResponse.sendRedirect(LoginServlet.ENDPOINT);
        }

    }

    @Override
    public void destroy() {

    }
}
