package ru.otus.webserver.web.servlet;

import ru.otus.webserver.core.dto.UserDto;
import ru.otus.webserver.core.model.User;
import ru.otus.webserver.core.service.DBServiceUser;
import ru.otus.webserver.web.ConstantsEndpoints;
import ru.otus.webserver.web.services.TemplateProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminServlet extends HttpServlet {


    private static final String ADMIN_PAGE_TEMPLATE = "adminpage.html";
    private static final String TEMPLATE_ATTR_RANDOM_USER = "users";
    private static final String PARAM_NEW_NAME = "newName";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_AGE = "age";

    private final DBServiceUser dbServiceUser;
    private final TemplateProcessor templateProcessor;

    public AdminServlet(DBServiceUser dbServiceUser, TemplateProcessor templateProcessor) {
        this.dbServiceUser = dbServiceUser;
        this.templateProcessor = templateProcessor;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<UserDto> users = dbServiceUser.getAllUsers().stream()
                .map(UserDto::fromUser)
                .collect(Collectors.toList());
        paramsMap.put(TEMPLATE_ATTR_RANDOM_USER, users);
        resp.setContentType("text/html");
        resp.getWriter().println(templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter(PARAM_NEW_NAME);
        String password = req.getParameter(PARAM_PASSWORD);
        int age = Integer.parseInt(req.getParameter(PARAM_AGE));
        User newUser = new User(0, name, age, password, false);
        dbServiceUser.saveUser(newUser);
        resp.sendRedirect(ConstantsEndpoints.ADMIN_ENDPOINT);//обновить страницу после добавления
    }
}
