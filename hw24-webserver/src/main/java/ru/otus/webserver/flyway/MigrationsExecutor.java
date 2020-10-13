package ru.otus.webserver.flyway;

public interface MigrationsExecutor {
    void cleanDb();

    void executeMigrations();
}
