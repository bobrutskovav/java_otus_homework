package ru.otus.ms.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.ms.app.controller.exception.UserNotFoundException;
import ru.otus.ms.app.core.dto.UserDto;
import ru.otus.ms.app.core.model.User;
import ru.otus.ms.app.core.service.DBServiceUser;

import java.io.IOException;

@Controller
public class UsersController {


    private final DBServiceUser dbServiceUser;


    public UsersController(DBServiceUser userDao) {
        this.dbServiceUser = userDao;
    }

    @GetMapping({"/users", "/"})
    public String getUsersPage(Model model) throws IOException {
        var randomUser = dbServiceUser.getRandomUser().orElseThrow(UserNotFoundException::new);
        var userDto = UserDto.fromUser(randomUser);
        var allUsers = dbServiceUser.getAllUsers();
        model.addAttribute("randomUser", userDto);
        model.addAttribute("users", allUsers);
        model.addAttribute("newUserDto", new UserDto());
        return "users";
    }


    @PostMapping("/users/create")
    public RedirectView createNewUser(@ModelAttribute UserDto userDto) {
        User user = UserDto.toUser(userDto);
        dbServiceUser.saveUser(user);
        return new RedirectView("/", true);
    }


}
