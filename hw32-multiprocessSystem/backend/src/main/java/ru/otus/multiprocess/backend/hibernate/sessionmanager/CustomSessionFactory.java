package ru.otus.multiprocess.backend.hibernate.sessionmanager;

public interface CustomSessionFactory {
    org.hibernate.SessionFactory getFactory();
}
