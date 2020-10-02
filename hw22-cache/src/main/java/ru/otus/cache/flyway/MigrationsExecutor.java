package ru.otus.cache.flyway;

public interface MigrationsExecutor {
    void cleanDb();

    void executeMigrations();
}
