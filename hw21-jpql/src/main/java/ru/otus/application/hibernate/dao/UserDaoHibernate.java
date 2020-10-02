package ru.otus.application.hibernate.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.application.core.dao.UserDao;
import ru.otus.application.core.dao.UserDaoException;
import ru.otus.application.core.model.User;
import ru.otus.application.core.sessionmanager.SessionManager;
import ru.otus.application.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.application.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

public class UserDaoHibernate implements UserDao {


    private static Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public Optional<User> findById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            currentSession.close();
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
        } finally {
            currentSession.close();
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
        } finally {
            currentSession.close();
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
        } finally {
            currentSession.close();
        }
    }


    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
