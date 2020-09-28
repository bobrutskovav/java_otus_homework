package ru.otus.application.jdbc.mapper;

/**
 * Создает SQL - запросы
 */
public interface EntitySQLMetaData {

    String getSelectByIdSql();

    String getInsertSql();

    String getSelectAllSql();


}
