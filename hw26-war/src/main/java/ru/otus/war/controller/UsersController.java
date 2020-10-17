package ru.otus.war.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.war.controller.exception.UserNotFoundException;
import ru.otus.war.core.dto.UserDto;
import ru.otus.war.core.service.DBServiceUser;

import java.io.IOException;

@Controller
public class UsersController {


    private static final String TEMPLATE_ATTR_RANDOM_USER = "randomUser";

    private final DBServiceUser dbServiceUser;

    public UsersController(DBServiceUser userDao) {
        this.dbServiceUser = userDao;
    }

    @GetMapping({"/users", "/"})
    public String getUsersPage(Model model) throws IOException {
        var randomUser = dbServiceUser.getRandomUser().orElseThrow(UserNotFoundException::new);
        var userDto = UserDto.fromUser(randomUser);
        model.addAttribute(TEMPLATE_ATTR_RANDOM_USER, userDto);
        return "users.html";
    }


}
