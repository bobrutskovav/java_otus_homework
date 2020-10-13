package ru.otus.webserver;

import com.google.gson.GsonBuilder;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.webserver.cachehw.HwListener;
import ru.otus.webserver.cachehw.MyCache;
import ru.otus.webserver.core.model.AddressDataSet;
import ru.otus.webserver.core.model.PhoneDataSet;
import ru.otus.webserver.core.model.User;
import ru.otus.webserver.core.service.DbServiceUserImpl;
import ru.otus.webserver.flyway.MigrationsExecutor;
import ru.otus.webserver.flyway.MigrationsExecutorFlyway;
import ru.otus.webserver.hibernate.HibernateUtils;
import ru.otus.webserver.hibernate.dao.UserDaoHibernate;
import ru.otus.webserver.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.webserver.web.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.webserver.web.services.TemplateProcessorImpl;
import ru.otus.webserver.web.services.UserAuthServiceImpl;

import javax.sql.DataSource;


public class WebServerHomeWork {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final Logger logger = LoggerFactory.getLogger(WebServerHomeWork.class);
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {


        MigrationsExecutor migrationsExecutor = new MigrationsExecutorFlyway(HIBERNATE_CFG_FILE);
        migrationsExecutor.executeMigrations();


        var sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class,
                AddressDataSet.class, PhoneDataSet.class);
        var sessionManager = new SessionManagerHibernate(sessionFactory);


        var userDao = new UserDaoHibernate(sessionManager);
        var cache = new MyCache<String, User>();
        var listener = new HwListener<String, User>() {
            @Override
            public void notify(String key, User value, String action) {
                logger.info("Action Performed KEY: {} VALUE: {} ACTON: {}", key, value, action);
            }
        };
        cache.addListener(listener);
        var dbServiceUser = new DbServiceUserImpl(userDao, cache);
        dbServiceUser.initDefaultUssrs();
        var gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        var templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        var authService = new UserAuthServiceImpl(dbServiceUser);

        var usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, userDao, gson, templateProcessor, dbServiceUser);

        usersWebServer.start();
        usersWebServer.join();
        cache.removeListener(listener);

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
