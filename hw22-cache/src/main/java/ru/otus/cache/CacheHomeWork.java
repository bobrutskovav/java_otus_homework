package ru.otus.cache;

import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.cachehw.HwCache;
import ru.otus.cache.cachehw.HwListener;
import ru.otus.cache.cachehw.MyCache;
import ru.otus.cache.core.dao.UserDao;
import ru.otus.cache.core.model.AddressDataSet;
import ru.otus.cache.core.model.PhoneDataSet;
import ru.otus.cache.core.model.User;
import ru.otus.cache.core.service.DbServiceUserImpl;
import ru.otus.cache.flyway.MigrationsExecutor;
import ru.otus.cache.flyway.MigrationsExecutorFlyway;
import ru.otus.cache.hibernate.HibernateUtils;
import ru.otus.cache.hibernate.dao.UserDaoHibernate;
import ru.otus.cache.hibernate.sessionmanager.SessionManagerHibernate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;


public class CacheHomeWork {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final Logger logger = LoggerFactory.getLogger(CacheHomeWork.class);

    public static void main(String[] args) {


        MigrationsExecutor migrationsExecutor = new MigrationsExecutorFlyway(HIBERNATE_CFG_FILE);
        migrationsExecutor.executeMigrations();
// Общая часть

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class,
                AddressDataSet.class, PhoneDataSet.class);
        var sessionManager = new SessionManagerHibernate(sessionFactory);

// Работа с пользователем

        UserDao userDao = new UserDaoHibernate(sessionManager);
        HwCache<String, User> cache = new MyCache<>();
        HwListener listener = new HwListener<String, User>() {
            @Override
            public void notify(String key, User value, String action) {
                logger.info("Action Performed KEY: {} VALUE: {} ACTON: {}", key, value, action);
            }
        };
        cache.addListener(listener);
// Код дальше должен остаться, т.е. userDao должен использоваться
        var dbServiceUser = new DbServiceUserImpl(userDao, cache);
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
                crUser -> logger.info("FROM CACHE created user, name:{}", crUser.getName()),
                () -> logger.info("user was not created")
        );


        cache.remove("1");
        user = dbServiceUser.getUser(id);

        user.ifPresentOrElse(
                crUser -> logger.info("FROM DB created user, name:{}", crUser.getName()),
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
