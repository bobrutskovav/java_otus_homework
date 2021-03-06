package ru.otus.war.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.war.controller.exception.UserNotFoundException;
import ru.otus.war.core.dto.UserDto;
import ru.otus.war.core.service.DBServiceUser;

@RestController
public class UsersApiController {


    private final DBServiceUser dbUserService;

    public UsersApiController(DBServiceUser dbUserService) {
        this.dbUserService = dbUserService;

    }

    @GetMapping("/api/user/{id}")
    protected UserDto getUser(@PathVariable long id) {
        return UserDto.fromUser(dbUserService.getUser(id).orElseThrow(UserNotFoundException::new));

    }


}
