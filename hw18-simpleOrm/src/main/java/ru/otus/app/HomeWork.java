package ru.otus.app;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.core.dao.UserDao;
import ru.otus.app.core.model.Account;
import ru.otus.app.core.model.User;
import ru.otus.app.core.service.DbAccountServiceImpl;
import ru.otus.app.core.service.DbServiceUserImpl;
import ru.otus.app.h2.DataSourceH2;
import ru.otus.app.jdbc.DbExecutorImpl;
import ru.otus.app.jdbc.dao.MyAccountDaoJdbc;
import ru.otus.app.jdbc.dao.MyUserDaoJdbc;
import ru.otus.app.jdbc.mapper.JdbcMapper;
import ru.otus.app.jdbc.mapper.MyJdbcMapperImpl;
import ru.otus.app.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Optional;


/**
 * Создайте в базе таблицу User с полями:
 * <p>
 * • id bigint(20) NOT NULL auto_increment
 * • name varchar(255)
 * • age int(3)
 * <p>
 * Создайте свою аннотацию @Id
 * <p>
 * Создайте класс User (с полями, которые соответствуют таблице, поле id отметьте аннотацией).
 * <p>
 * Реализуйте интерфейс JdbcMapper<T>, который умеет работать с классами, в которых есть поле с аннотацией @Id.
 * JdbcMapper<T> должен сохранять объект в базу и читать объект из базы.
 * Для этого надо реализовать оставшиеся интерфейсы из пакета mapper.
 * Таким обзазом, получится надстройка над DbExecutor<T>, которая по заданному классу умеет генерировать sql-запросы.
 * А DbExecutor<T> должен выполнять сгенерированные запросы.
 * <p>
 * имя таблицы должно соответствовать имени класса, а поля класса - это колонки в таблице.
 * <p>
 * Проверьте его работу на классе User.
 * <p>
 * За основу возьмите класс HomeWork.
 * <p>
 * Создайте еще одну таблицу Account:
 * • no bigint(20) NOT NULL auto_increment
 * • type varchar(255)
 * • rest number
 * <p>
 * Создайте для этой таблицы класс Account и проверьте работу JdbcMapper на этом классе
 */

public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
// Общая часть
        var dataSource = new DataSourceH2();
        flywayMigrations(dataSource);
        var sessionManager = new SessionManagerJdbc(dataSource);

// Работа с пользователем
        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        JdbcMapper<User> jdbcMapperUser = new MyJdbcMapperImpl<>(User.class, sessionManager, dbExecutor); //
        UserDao userDao = new MyUserDaoJdbc(sessionManager, jdbcMapperUser); // = new UserDaoJdbcMapper(sessionManager, dbExecutor);

// Код дальше должен остаться, т.е. userDao должен использоваться
        var dbServiceUser = new DbServiceUserImpl(userDao);
        var id = dbServiceUser.saveUser(new User(0, "dbServiceUser", 13));
        Optional<User> user = dbServiceUser.getUser(id);

        user.ifPresentOrElse(
                crUser -> logger.info("created user, name:{}", crUser.getName()),
                () -> logger.info("user was not created")
        );
// Работа со счетом
        var accountJdbcMapper = new MyJdbcMapperImpl<>(Account.class, sessionManager, new DbExecutorImpl<>());
        var accountDao = new MyAccountDaoJdbc(sessionManager, accountJdbcMapper);
        var accountService = new DbAccountServiceImpl(accountDao);
        var no = accountService.saveAccount(new Account(12L, "newAccount!", new BigDecimal("13.2")));
        Optional<Account> account = accountService.getAccount(no);

        account.ifPresentOrElse(
                account1 -> logger.info("created account, name:{}", account1.getType()),
                () -> logger.info("Account was not created")
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
