package ru.otus.webserver.web.server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.webserver.core.dao.UserDao;
import ru.otus.webserver.core.service.DBServiceUser;
import ru.otus.webserver.web.services.TemplateProcessor;
import ru.otus.webserver.web.services.UserAuthService;
import ru.otus.webserver.web.servlet.AdminAuthorizationFilter;
import ru.otus.webserver.web.servlet.AuthorizationFilter;
import ru.otus.webserver.web.servlet.LoginServlet;

import java.util.Arrays;

public class UsersWebServerWithFilterBasedSecurity extends UsersWebServerSimple {
    private final UserAuthService authService;

    public UsersWebServerWithFilterBasedSecurity(int port,
                                                 UserAuthService authService,
                                                 UserDao userDao,
                                                 Gson gson,
                                                 TemplateProcessor templateProcessor,
                                                 DBServiceUser dbServiceUser) {
        super(port, userDao, gson, templateProcessor, dbServiceUser);
        this.authService = authService;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        servletContextHandler.addFilter(new FilterHolder(new AdminAuthorizationFilter()), "/adminpage", null);
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}
