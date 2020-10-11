package ru.otus.webserver.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.webserver.cachehw.HwCache;
import ru.otus.webserver.core.dao.UserDao;
import ru.otus.webserver.core.model.AddressDataSet;
import ru.otus.webserver.core.model.PhoneDataSet;
import ru.otus.webserver.core.model.User;

import java.util.ArrayList;
import java.util.List;
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
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    cache.put(String.valueOf(userOptional.get().getId()), user);
                }
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

    public void initDefaultUssrs() {
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
