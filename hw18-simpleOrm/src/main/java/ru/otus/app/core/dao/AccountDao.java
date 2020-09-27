package ru.otus.app.core.dao;

import ru.otus.app.core.model.Account;
import ru.otus.app.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface AccountDao {

    Optional<Account> findByNo(long id);

    long insertAccount(Account account);


    SessionManager getSessionManager();
}
