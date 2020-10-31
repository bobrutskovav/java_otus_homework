package ru.otus.war.hibernate.sessionmanager;

public interface CustomSessionFactory {
    org.hibernate.SessionFactory getFactory();
}
