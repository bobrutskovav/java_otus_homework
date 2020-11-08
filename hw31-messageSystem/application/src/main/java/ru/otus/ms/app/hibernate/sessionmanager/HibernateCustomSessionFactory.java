package ru.otus.ms.app.hibernate.sessionmanager;

import org.hibernate.SessionFactory;

public class HibernateCustomSessionFactory implements CustomSessionFactory {


    private SessionFactory sessionFactory;

    public HibernateCustomSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public SessionFactory getFactory() {
        return sessionFactory;
    }
}
