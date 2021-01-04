package ru.otus.multiprocess.backend.hibernate.sessionmanager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import ru.otus.multiprocess.backend.core.sessionmanager.SessionManager;
import ru.otus.multiprocess.backend.core.sessionmanager.SessionManagerException;

@Component
public class SessionManagerHibernate implements SessionManager {

    private final SessionFactory sessionFactory;
    private DatabaseSessionHibernate databaseSession;

    public SessionManagerHibernate(CustomSessionFactory sessionFactory) {
        if (sessionFactory == null) {
            throw new SessionManagerException("SessionFactory is null");
        }
        this.sessionFactory = sessionFactory.getFactory();
    }

    @Override
    public void beginSession() {
        try {
            databaseSession = new DatabaseSessionHibernate(sessionFactory.openSession());
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void commitSession() {
        checkSessionAndTransaction();
        try {
            databaseSession.getTransaction().commit();
            databaseSession.getHibernateSession().close();
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void rollbackSession() {
        checkSessionAndTransaction();
        try {
            databaseSession.getTransaction().rollback();
            databaseSession.getHibernateSession().close();
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void close() {
        if (databaseSession == null) {
            return;
        }
        Session session = databaseSession.getHibernateSession();
        if (session == null || !session.isConnected()) {
            return;
        }

        Transaction transaction = databaseSession.getTransaction();
        if (transaction == null || !transaction.isActive()) {
            return;
        }

        try {
            databaseSession.close();
            databaseSession = null;
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public DatabaseSessionHibernate getCurrentSession() {
        checkSessionAndTransaction();
        return databaseSession;
    }

    private void checkSessionAndTransaction() {
        if (databaseSession == null) {
            databaseSession = new DatabaseSessionHibernate(sessionFactory.openSession());
        }
        Session session = databaseSession.getHibernateSession();
        if (session == null || !session.isConnected()) {
            databaseSession = new DatabaseSessionHibernate(sessionFactory.openSession());
        }

        Transaction transaction = databaseSession.getTransaction();
        if (transaction == null || !transaction.isActive()) {
            throw new SessionManagerException("Transaction not opened ");
        }
    }
}
