package ru.otus.application.jdbc.dao;

import ru.otus.application.core.dao.UserDao;
import ru.otus.application.core.model.User;
import ru.otus.application.core.sessionmanager.SessionManager;
import ru.otus.application.jdbc.mapper.JdbcMapper;
import ru.otus.application.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class MyUserDaoJdbc implements UserDao {


    private SessionManager sessionManager;
    private JdbcMapper<User> jdbcMapper;

    public MyUserDaoJdbc(SessionManagerJdbc sessionManager, JdbcMapper<User> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.of(jdbcMapper.findById(id, User.class));
    }

    @Override
    public long insertUser(User user) {
        jdbcMapper.insert(user);
        return user.getId();
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
