package ru.otus.war.flyway;

public interface MigrationsExecutor {
    void cleanDb();

    void executeMigrations();
}
