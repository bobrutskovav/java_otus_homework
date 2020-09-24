package ru.otus.app.jdbc.mapper.impl;

import ru.otus.app.jdbc.mapper.EntityClassMetaData;
import ru.otus.app.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

import static ru.otus.app.helper.StringHelper.stripPackageName;

public class EntitySqlMetadataImpl<T> implements EntitySQLMetaData {

    private final EntityClassMetaData<T> entityClassMetaData;


    private final String tableName;

    private String selectAllSql;
    private String selectByIdSql;
    private String insertSql;


    public EntitySqlMetadataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
        tableName = stripPackageName(entityClassMetaData.getName()).toUpperCase();
    }

    @Override
    public String getSelectAllSql() {
        if (selectAllSql == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT * FROM ").append(tableName).append(";");
            selectAllSql = builder.toString();
        }

        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {

        if (selectByIdSql == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT * FROM ")
                    .append(tableName).append(" WHERE ")
                    .append(tableName).append(".").append(entityClassMetaData.getIdField().getName())
                    .append(" = ?;");
            selectByIdSql = builder.toString();
        }
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        if (insertSql == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("INSERT INTO ").append(tableName);
            String columnNames = entityClassMetaData.getAllFields().stream().map(Field::getName).collect(Collectors.joining(",", " (", ") "));
            builder.append(columnNames).append("VALUES ");
            String valuesVars = entityClassMetaData.getAllFields().stream().map(field -> "?").collect(Collectors.joining(",", " (", ")"));
            builder.append(valuesVars).append(";");
            insertSql = builder.toString();
        }
        return insertSql;

    }

}
