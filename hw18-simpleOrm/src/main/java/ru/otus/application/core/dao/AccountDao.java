package ru.otus.application.core.dao;

import ru.otus.application.core.model.Account;
import ru.otus.application.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface AccountDao {

    Optional<Account> findByNo(long id);

    long insertAccount(Account account);


    SessionManager getSessionManager();
}
