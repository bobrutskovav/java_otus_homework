package ru.otus.webserver.web.server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.webserver.core.dao.UserDao;
import ru.otus.webserver.core.service.DBServiceUser;
import ru.otus.webserver.web.ConstantsEndpoints;
import ru.otus.webserver.web.helpers.FileSystemHelper;
import ru.otus.webserver.web.services.TemplateProcessor;
import ru.otus.webserver.web.servlet.AdminServlet;
import ru.otus.webserver.web.servlet.UsersApiServlet;
import ru.otus.webserver.web.servlet.UsersServlet;

public class UsersWebServerSimple implements UsersWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";
    protected final TemplateProcessor templateProcessor;
    private final UserDao userDao;
    private final Gson gson;
    private final Server server;
    private final DBServiceUser dbServiceUser;

    public UsersWebServerSimple(int port, UserDao userDao, Gson gson, TemplateProcessor templateProcessor, DBServiceUser dbServiceUser) {
        this.userDao = userDao;
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/users", "/api/users/*"));


        server.setHandler(handlers);
        return server;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new AdminServlet(dbServiceUser, templateProcessor)), ConstantsEndpoints.ADMIN_ENDPOINT);
        servletContextHandler.addServlet(new ServletHolder(new UsersServlet(templateProcessor, userDao)), ConstantsEndpoints.USERS_ENDPOINT);
        servletContextHandler.addServlet(new ServletHolder(new UsersApiServlet(userDao, gson)), ConstantsEndpoints.API_ENDPOINT + "/user/*");
        return servletContextHandler;
    }
}
