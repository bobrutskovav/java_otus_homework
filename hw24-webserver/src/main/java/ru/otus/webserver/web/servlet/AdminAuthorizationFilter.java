package ru.otus.webserver.web.servlet;

import ru.otus.webserver.web.ConstantsEndpoints;

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
            boolean isAdmin = Boolean.parseBoolean(String.valueOf(session.getAttribute(LoginServlet.ADMIN_SESSION_ATTRIBUTE)));
            if (!isAdmin) {
                httpServletResponse.sendRedirect(ConstantsEndpoints.LOGIN_ENDPOINT);
            } else {
                chain.doFilter(httpServletRequest, httpServletResponse);
            }
        } else {
            httpServletResponse.sendRedirect(ConstantsEndpoints.LOGIN_ENDPOINT);
        }

    }

    @Override
    public void destroy() {

    }
}
