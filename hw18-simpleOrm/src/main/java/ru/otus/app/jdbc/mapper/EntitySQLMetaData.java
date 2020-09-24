package ru.otus.app.jdbc.mapper;

/**
 * Создает SQL - запросы
 */
public interface EntitySQLMetaData {

    String getSelectByIdSql();

    String getInsertSql();

    String getSelectAllSql();


}
