package ru.otus.webserver.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.webserver.core.dao.UserDao;
import ru.otus.webserver.core.dao.UserDaoException;
import ru.otus.webserver.core.model.User;
import ru.otus.webserver.core.sessionmanager.SessionManager;
import ru.otus.webserver.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.webserver.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Optional.ofNullable;

public class UserDaoHibernate implements UserDao {


    private static Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public Optional<User> findByName(String name) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {

            Query<User> query = currentSession.getHibernateSession().createQuery("SELECT u FROM User u WHERE u.name = :name", User.class);
            query.setParameter("name", name);
            return Optional.ofNullable(query.uniqueResult());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return ofNullable(currentSession.getHibernateSession().find(User.class, id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long insertUser(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(user);
            hibernateSession.flush();
            return user.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void updateUser(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void insertOrUpdate(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (user.getId() > 0) {
                hibernateSession.merge(user);
            } else {
                hibernateSession.persist(user);
                hibernateSession.flush();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }


    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    @Override
    public Optional<User> findRandomUser() {
        List<User> allUsers = this.findAllUsers();
        int index = ThreadLocalRandom.current().nextInt(allUsers.size());
        return allUsers.isEmpty() ? Optional.empty() : Optional.of(allUsers.get(index));
    }

    @Override
    public List<User> findAllUsers() {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        List<User> users = null;
        try {
            Query<User> query = currentSession.getHibernateSession().createQuery("SELECT u FROM User u", User.class);
            users = query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return users;
    }
}
