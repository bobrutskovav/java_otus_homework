package ru.otus.ms.app.flyway;

public interface MigrationsExecutor {
    void cleanDb();

    void executeMigrations();
}
