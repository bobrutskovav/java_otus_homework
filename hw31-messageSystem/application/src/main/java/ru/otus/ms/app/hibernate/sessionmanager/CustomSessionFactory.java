package ru.otus.ms.app.hibernate.sessionmanager;

public interface CustomSessionFactory {
    org.hibernate.SessionFactory getFactory();
}
