package ru.otus.webserver.core.dao;


import ru.otus.webserver.core.model.User;
import ru.otus.webserver.core.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findByName(String name);

    Optional<User> findById(long id);

    long insertUser(User user);

    void updateUser(User user);

    void insertOrUpdate(User user);

    SessionManager getSessionManager();

    Optional<User> findRandomUser();

    List<User> findAllUsers();
}
