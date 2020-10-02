package ru.otus.cache.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.cachehw.HwCache;
import ru.otus.cache.core.dao.UserDao;
import ru.otus.cache.core.model.User;

import java.util.Optional;

public class DbServiceUserImpl implements DBServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDao userDao;

    private final HwCache<String, User> cache;

    public DbServiceUserImpl(UserDao userDao, HwCache<String, User> hwCache) {
        cache = hwCache;
        this.userDao = userDao;
    }

    @Override
    public long saveUser(User user) {
        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var userId = userDao.insertUser(user);
                cache.put(String.valueOf(userId), user);
                sessionManager.commitSession();

                logger.info("created user: {}", userId);
                return userId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            } finally {
                sessionManager.close();
            }
        }
    }

    @Override
    public Optional<User> getUser(long id) {
        User cachedUser = cache.get(String.valueOf(id));
        if (cachedUser != null) {
            return Optional.of(cachedUser);
        }
        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findById(id);

                logger.info("user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            } finally {
                sessionManager.close();
            }
            return Optional.empty();
        }
    }
}
