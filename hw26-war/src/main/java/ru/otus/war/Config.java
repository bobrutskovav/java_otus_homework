package ru.otus.war;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.war.cachehw.HwCache;
import ru.otus.war.cachehw.MyCache;
import ru.otus.war.core.model.AddressDataSet;
import ru.otus.war.core.model.PhoneDataSet;
import ru.otus.war.core.model.User;
import ru.otus.war.hibernate.HibernateUtils;
import ru.otus.war.hibernate.sessionmanager.CustomSessionFactory;
import ru.otus.war.hibernate.sessionmanager.HibernateCustomSessionFactory;

@Configuration
@ComponentScan
public class Config {

    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    @Bean
    public HwCache<String, User> userHwCache() {
        return new MyCache<>();
    }

    @Bean
    public CustomSessionFactory sessionFactory() {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE,
                User.class, AddressDataSet.class, PhoneDataSet.class);
        return new HibernateCustomSessionFactory(sessionFactory);
    }
}
