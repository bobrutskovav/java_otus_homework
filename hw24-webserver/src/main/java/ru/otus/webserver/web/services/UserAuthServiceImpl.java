package ru.otus.webserver.web.services;

import ru.otus.webserver.core.service.DBServiceUser;

public class UserAuthServiceImpl implements UserAuthService {

    private final DBServiceUser dbServiceUser;

    public UserAuthServiceImpl(DBServiceUser userDao) {
        this.dbServiceUser = userDao;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return dbServiceUser.getUserByName(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    @Override
    public boolean authenticateAdmin(String login, String password) {
        return dbServiceUser.getUserByName(login)
                .map(user -> user.getPassword().equals(password) && user.isAdmin())
                .orElse(false);

    }
}
