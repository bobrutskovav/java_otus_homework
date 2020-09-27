package ru.otus.app.core.service;

import ru.otus.app.core.model.Account;

import java.util.Optional;

public interface DbAccountService {


    long saveAccount(Account account);

    Optional<Account> getAccount(long no);
}
