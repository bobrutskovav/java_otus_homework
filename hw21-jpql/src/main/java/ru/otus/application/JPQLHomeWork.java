package ru.otus.application;

import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.application.core.dao.UserDao;
import ru.otus.application.core.model.AddressDataSet;
import ru.otus.application.core.model.PhoneDataSet;
import ru.otus.application.core.model.User;
import ru.otus.application.core.service.DbServiceUserImpl;
import ru.otus.application.hibernate.HibernateUtils;
import ru.otus.application.hibernate.dao.UserDaoHibernate;
import ru.otus.application.hibernate.sessionmanager.SessionManagerHibernate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;


public class JPQLHomeWork {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final Logger logger = LoggerFactory.getLogger(JPQLHomeWork.class);

    public static void main(String[] args) {


        //   MigrationsExecutor migrationsExecutor = new MigrationsExecutorFlyway(HIBERNATE_CFG_FILE);
        //   migrationsExecutor.executeMigrations();
// Общая часть

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class,
                AddressDataSet.class, PhoneDataSet.class);
        var sessionManager = new SessionManagerHibernate(sessionFactory);

// Работа с пользователем

        UserDao userDao = new UserDaoHibernate(sessionManager);

// Код дальше должен остаться, т.е. userDao должен использоваться
        var dbServiceUser = new DbServiceUserImpl(userDao);
        User newUser = new User(0, "dbServiceUser", 13);
        AddressDataSet addressDataSet = new AddressDataSet();
        addressDataSet.setStreet("USER STREET");
        newUser.setAddressDataSet(addressDataSet);
        PhoneDataSet phoneA = new PhoneDataSet();
        phoneA.setUser(newUser);
        phoneA.setNumber("+700909090");
        PhoneDataSet phoneB = new PhoneDataSet();
        phoneB.setUser(newUser);
        phoneB.setNumber("+99999999");
        List<PhoneDataSet> userPhones = List.of(phoneA, phoneB);
        newUser.setPhoneDataSets(userPhones);
        var id = dbServiceUser.saveUser(newUser);
        Optional<User> user = dbServiceUser.getUser(id);

        user.ifPresentOrElse(
                crUser -> logger.info("created user, name:{}", crUser.getName()),
                () -> logger.info("user was not created")
        );

    }

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}
