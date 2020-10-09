package ru.otus.webserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.webserver.cachehw.HwCache;
import ru.otus.webserver.cachehw.HwListener;
import ru.otus.webserver.cachehw.MyCache;
import ru.otus.webserver.core.dao.UserDao;
import ru.otus.webserver.core.model.AddressDataSet;
import ru.otus.webserver.core.model.PhoneDataSet;
import ru.otus.webserver.core.model.User;
import ru.otus.webserver.core.service.DbServiceUserImpl;
import ru.otus.webserver.flyway.MigrationsExecutor;
import ru.otus.webserver.flyway.MigrationsExecutorFlyway;
import ru.otus.webserver.hibernate.HibernateUtils;
import ru.otus.webserver.hibernate.dao.UserDaoHibernate;
import ru.otus.webserver.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.webserver.web.server.UsersWebServer;
import ru.otus.webserver.web.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.webserver.web.services.TemplateProcessor;
import ru.otus.webserver.web.services.TemplateProcessorImpl;
import ru.otus.webserver.web.services.UserAuthService;
import ru.otus.webserver.web.services.UserAuthServiceImpl;

import javax.sql.DataSource;
import java.util.List;


public class WebServerHomeWork {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final Logger logger = LoggerFactory.getLogger(WebServerHomeWork.class);
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {


        MigrationsExecutor migrationsExecutor = new MigrationsExecutorFlyway(HIBERNATE_CFG_FILE);
        migrationsExecutor.executeMigrations();


        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class,
                AddressDataSet.class, PhoneDataSet.class);
        var sessionManager = new SessionManagerHibernate(sessionFactory);


        UserDao userDao = new UserDaoHibernate(sessionManager);
        HwCache<String, User> cache = new MyCache<>();
        HwListener listener = new HwListener<String, User>() {
            @Override
            public void notify(String key, User value, String action) {
                logger.info("Action Performed KEY: {} VALUE: {} ACTON: {}", key, value, action);
            }
        };
        cache.addListener(listener);
        var dbServiceUser = new DbServiceUserImpl(userDao, cache);
        User newUser = new User(0, "NotAdmin", 13, "password", false);
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
        dbServiceUser.saveUser(newUser);

        User admin = new User(0, "Admin", 13, "password", true);
        dbServiceUser.saveUser(admin);
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
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
