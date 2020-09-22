package ru.otus.app.jdbc.dao;

import ru.otus.app.core.dao.AccountDao;
import ru.otus.app.core.model.Account;
import ru.otus.app.core.sessionmanager.SessionManager;
import ru.otus.app.jdbc.mapper.JdbcMapper;
import ru.otus.app.jdbc.sessionmanager.SessionManagerJdbc;

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
