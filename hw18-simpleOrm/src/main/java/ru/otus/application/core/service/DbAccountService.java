package ru.otus.application.core.service;

import ru.otus.application.core.model.Account;

import java.util.Optional;

public interface DbAccountService {


    long saveAccount(Account account);

    Optional<Account> getAccount(long no);
}
