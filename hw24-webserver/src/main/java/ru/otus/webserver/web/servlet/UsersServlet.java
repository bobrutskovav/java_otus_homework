package ru.otus.webserver.web.servlet;

import ru.otus.webserver.core.dao.UserDao;
import ru.otus.webserver.core.dto.UserDto;
import ru.otus.webserver.web.services.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class UsersServlet extends HttpServlet {


    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_RANDOM_USER = "randomUser";

    private final UserDao userDao;
    private final TemplateProcessor templateProcessor;

    public UsersServlet(TemplateProcessor templateProcessor, UserDao userDao) {
        this.templateProcessor = templateProcessor;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        UserDto userDto = userDao.findRandomUser()
                .map(UserDto::fromUser)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        paramsMap.put(TEMPLATE_ATTR_RANDOM_USER, userDto);
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

}
