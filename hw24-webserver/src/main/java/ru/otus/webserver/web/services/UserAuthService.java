package ru.otus.webserver.web.services;

public interface UserAuthService {
    boolean authenticate(String login, String password);

    boolean authenticateAdmin(String login, String password);
}
