package ru.otus.multiprocess.backend.core.dao;


import ru.otus.multiprocess.backend.core.model.User;
import ru.otus.multiprocess.backend.core.sessionmanager.SessionManager;

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
