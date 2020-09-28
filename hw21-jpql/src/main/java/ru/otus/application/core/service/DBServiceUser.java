package ru.otus.application.core.service;

import ru.otus.application.core.model.User;

import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);
}
