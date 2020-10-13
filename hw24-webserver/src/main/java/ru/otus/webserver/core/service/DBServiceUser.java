package ru.otus.webserver.core.service;

import ru.otus.webserver.core.model.User;

import java.util.List;
import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    List<User> getAllUsers();

    Optional<User> getUserByName(String name);


}
