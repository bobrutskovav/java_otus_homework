package ru.otus.app.jdbc.mapper.impl;

import ru.otus.app.jdbc.mapper.EntityClassMetaData;
import ru.otus.app.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

import static ru.otus.app.helper.StringHelper.stripPackageName;

public class EntitySqlMetadataImpl<T> implements EntitySQLMetaData {

    private EntityClassMetaData<T> entityClassMetaData;


    private String tableName;


    public EntitySqlMetadataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
        tableName = stripPackageName(entityClassMetaData.getName()).toUpperCase();
    }

    @Override
    public String getSelectAllSql() {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM ").append(tableName).append(";");
        return builder.toString();
    }

    @Override
    public String getSelectByIdSql() {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM ")
                .append(tableName).append(" WHERE ")
                .append(tableName).append(".").append(entityClassMetaData.getIdField().getName())
                .append(" = ?;");

        return builder.toString();
    }

    @Override
    public String getInsertSql() {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ").append(tableName);

        String columnNames = entityClassMetaData.getAllFields().stream().map(Field::getName).collect(Collectors.joining(",", " (", ") "));
        builder.append(columnNames).append("VALUES ");
        String valuesVars = entityClassMetaData.getAllFields().stream().map(field -> "?").collect(Collectors.joining(",", " (", ")"));
        builder.append(valuesVars).append(";");
        return builder.toString();
    }

    @Override
    public String getUpdateSql() {
        return null;
    }

}
