package ru.otus.multiprocess.backend.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.otus.multiprocess.backend.core.dao.UserDao;
import ru.otus.multiprocess.backend.core.dao.UserDaoException;
import ru.otus.multiprocess.backend.core.model.User;
import ru.otus.multiprocess.backend.core.sessionmanager.SessionManager;
import ru.otus.multiprocess.backend.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.multiprocess.backend.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Optional.ofNullable;

@Repository
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
        if (allUsers.isEmpty()) {
            return Optional.empty();
        } else {
            int index = ThreadLocalRandom.current().nextInt(allUsers.size());
            return Optional.ofNullable(allUsers.get(index));
        }

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
