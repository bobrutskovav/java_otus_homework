package ru.otus.webserver.web.services;

import ru.otus.webserver.core.dao.UserDao;
import ru.otus.webserver.core.model.User;

import java.util.Optional;

public class UserAuthServiceImpl implements UserAuthService {

    private final UserDao userDao;

    public UserAuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return userDao.findByName(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    @Override
    public boolean authenticateAdmin(String login, String password) {
        Optional<User> user = userDao.findByName(login);
        if (user.isPresent()) {
            User u = user.get();
            return u.getPassword().equals(password) && u.isAdmin();
        }
        return false;
    }
}
