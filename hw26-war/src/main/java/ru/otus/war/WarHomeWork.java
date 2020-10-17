//package ru.otus.war;
//
//import com.google.gson.GsonBuilder;
//import org.flywaydb.core.Flyway;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import ru.otus.war.cachehw.HwListener;
//import ru.otus.war.cachehw.MyCache;
//import ru.otus.war.core.model.AddressDataSet;
//import ru.otus.war.core.model.PhoneDataSet;
//import ru.otus.war.core.model.User;
//import ru.otus.war.core.service.DbServiceUserImpl;
//import ru.otus.war.flyway.MigrationsExecutor;
//import ru.otus.war.flyway.MigrationsExecutorFlyway;
//import ru.otus.war.hibernate.HibernateUtils;
//import ru.otus.war.hibernate.dao.UserDaoHibernate;
//import ru.otus.war.hibernate.sessionmanager.SessionManagerHibernate;
//
//import javax.sql.DataSource;
//
//
//public class WarHomeWork {
//
//    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
//    private static final Logger logger = LoggerFactory.getLogger(WarHomeWork.class);
//    private static final int WEB_SERVER_PORT = 8080;
//    private static final String TEMPLATES_DIR = "/templates/";
//
//    public static void main(String[] args) throws Exception {
//
//
//        MigrationsExecutor migrationsExecutor = new MigrationsExecutorFlyway(HIBERNATE_CFG_FILE);
//        migrationsExecutor.executeMigrations();
//
//
//        var sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class,
//                AddressDataSet.class, PhoneDataSet.class);
//        var sessionManager = new SessionManagerHibernate(sessionFactory);
//
//
//        var userDao = new UserDaoHibernate(sessionManager);
//        var cache = new MyCache<String, User>();
//        var listener = new HwListener<String, User>() {
//            @Override
//            public void notify(String key, User value, String action) {
//                logger.info("Action Performed KEY: {} VALUE: {} ACTON: {}", key, value, action);
//            }
//        };
//        cache.addListener(listener);
//        var dbServiceUser = new DbServiceUserImpl(userDao, cache);
//        dbServiceUser.initDefaultUssrs();
//        cache.removeListener(listener);
//
//    }
//
//    private static void flywayMigrations(DataSource dataSource) {
//        logger.info("db migration started...");
//        var flyway = Flyway.configure()
//                .dataSource(dataSource)
//                .locations("classpath:/db/migration")
//                .load();
//        flyway.migrate();
//        logger.info("db migration finished.");
//        logger.info("***");
//    }
//}
