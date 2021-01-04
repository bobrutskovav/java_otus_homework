package ru.otus.multiprocess.backend.core.service;

import ru.otus.multiprocess.backend.core.model.User;

import java.util.List;
import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    List<User> getAllUsers();

    Optional<User> getUserByName(String name);

    Optional<User> getRandomUser();


}
