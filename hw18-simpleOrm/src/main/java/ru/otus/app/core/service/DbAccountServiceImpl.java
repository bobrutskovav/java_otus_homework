package ru.otus.app.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.core.dao.AccountDao;
import ru.otus.app.core.model.Account;

import java.util.Optional;

public class DbAccountServiceImpl implements DbAccountService {

    private static final Logger log = LoggerFactory.getLogger(DbAccountServiceImpl.class);

    private AccountDao accountDao;

    public DbAccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public long saveAccount(Account account) {
        try (var sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var userId = accountDao.insertAccount(account);
                sessionManager.commitSession();

                log.info("created Account: {}", userId);
                return userId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<Account> getAccount(long no) {
        try (var sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<Account> accountOptional = accountDao.findByNo(no);

                log.info("Account type: {}", accountOptional.orElse(null));
                return accountOptional;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}
