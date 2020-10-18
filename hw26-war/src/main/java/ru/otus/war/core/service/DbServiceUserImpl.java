package ru.otus.war.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.war.cachehw.HwCache;
import ru.otus.war.core.dao.UserDao;
import ru.otus.war.core.model.AddressDataSet;
import ru.otus.war.core.model.PhoneDataSet;
import ru.otus.war.core.model.User;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
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
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    logger.info("user: {}", user);
                    cache.put(String.valueOf(userOptional.get().getId()), user);
                }
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

    @Override
    public List<User> getAllUsers() {

        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                return userDao.findAllUsers();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                return new ArrayList<>();
            } finally {
                sessionManager.close();
            }
        }
    }

    @Override
    public Optional<User> getUserByName(String name) {
        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                return userDao.findByName(name);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                return Optional.empty();
            } finally {
                sessionManager.close();
            }
        }
    }

    @Override
    public Optional<User> getRandomUser() {

        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                return userDao.findRandomUser();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                return Optional.empty();
            } finally {
                sessionManager.close();
            }
        }
    }

    @PostConstruct
    public void initDefaultUsers() {
        User newUser = new User(0, "NotAdmin", 13, "password", false);
        AddressDataSet addressDataSet = new AddressDataSet();
        addressDataSet.setStreet("USER STREET");
        newUser.setAddressDataSet(addressDataSet);
        PhoneDataSet phoneA = new PhoneDataSet();
        phoneA.setUser(newUser);
        phoneA.setNumber("+700909090");
        PhoneDataSet phoneB = new PhoneDataSet();
        phoneB.setUser(newUser);
        phoneB.setNumber("+99999999");
        List<PhoneDataSet> userPhones = List.of(phoneA, phoneB);
        newUser.setPhoneDataSets(userPhones);
        saveUser(newUser);

        User admin = new User(0, "Admin", 13, "password", true);
        saveUser(admin);
    }
}
