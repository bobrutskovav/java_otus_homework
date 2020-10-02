package ru.otus.application.jdbc.dao;

import ru.otus.application.core.dao.AccountDao;
import ru.otus.application.core.model.Account;
import ru.otus.application.core.sessionmanager.SessionManager;
import ru.otus.application.jdbc.mapper.JdbcMapper;
import ru.otus.application.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class MyAccountDaoJdbc implements AccountDao {

    private SessionManager sessionManager;
    private JdbcMapper<Account> jdbcMapper;

    public MyAccountDaoJdbc(SessionManagerJdbc sessionManager, JdbcMapper<Account> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }


    @Override
    public Optional<Account> findByNo(long no) {
        return Optional.of(jdbcMapper.findById(no, Account.class));
    }

    @Override
    public long insertAccount(Account account) {
        jdbcMapper.insert(account);
        return account.getNo();
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
