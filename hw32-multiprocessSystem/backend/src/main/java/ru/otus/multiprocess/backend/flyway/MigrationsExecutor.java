package ru.otus.multiprocess.backend.flyway;

public interface MigrationsExecutor {
    void cleanDb();

    void executeMigrations();
}
