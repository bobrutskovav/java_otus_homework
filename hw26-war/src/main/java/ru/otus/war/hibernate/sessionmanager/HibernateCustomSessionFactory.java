package ru.otus.war.hibernate.sessionmanager;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import ru.otus.war.core.model.AddressDataSet;
import ru.otus.war.core.model.PhoneDataSet;
import ru.otus.war.core.model.User;
import ru.otus.war.hibernate.HibernateUtils;

@Component
public class HibernateCustomSessionFactory implements CustomSessionFactory {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    private SessionFactory sessionFactory;

    public HibernateCustomSessionFactory() {
        sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class, AddressDataSet.class, PhoneDataSet.class);
    }

    @Override
    public SessionFactory getFactory() {
        return sessionFactory;
    }
}
