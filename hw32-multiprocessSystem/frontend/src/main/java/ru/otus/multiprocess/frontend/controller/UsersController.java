package ru.otus.multiprocess.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.multiprocess.backend.core.dto.UserDto;
import ru.otus.multiprocess.backend.core.model.User;

import java.io.IOException;
import java.util.List;

@Controller
public class UsersController {


    @GetMapping({"/users", "/"})
    public String getUsersPage(Model model) throws IOException {
        var randomUser = new User(1, "FAKED RANDOM USER", 23, "azaza", false);
        var userDto = UserDto.fromUser(randomUser);
        var allUsers = List.of(new User(2, "FAKED USER 1", 23, "azaza", false),
                new User(3, "FAKED RANDOM USER", 23, "azaza", false));
        model.addAttribute("randomUser", userDto);
        model.addAttribute("users", allUsers);
        model.addAttribute("newUserDto", new UserDto());
        return "users";
    }


    @PostMapping("/users/create")
    public RedirectView createNewUser(@ModelAttribute UserDto userDto) {
        User user = UserDto.toUser(userDto);
        return new RedirectView("/", true);
    }


}
