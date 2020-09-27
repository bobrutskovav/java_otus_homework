package ru.otus.app.core.dao;


import ru.otus.app.core.model.User;
import ru.otus.app.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    long insertUser(User user);


    SessionManager getSessionManager();
}
