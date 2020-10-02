package ru.otus.application.flyway;

public interface MigrationsExecutor {
    void cleanDb();

    void executeMigrations();
}
