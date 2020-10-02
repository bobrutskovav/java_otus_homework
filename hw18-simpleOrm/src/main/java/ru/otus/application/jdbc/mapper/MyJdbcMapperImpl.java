package ru.otus.application.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.application.jdbc.DbExecutor;
import ru.otus.application.jdbc.mapper.impl.EntitySqlMetadataImpl;
import ru.otus.application.jdbc.mapper.impl.ReflectionEntityClassMetaData;
import ru.otus.application.jdbc.sessionmanager.SessionManagerJdbc;
import ru.otus.application.reflection.ReflectionHelper;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MyJdbcMapperImpl<T> implements JdbcMapper<T> {

    private Logger log = LoggerFactory.getLogger(MyJdbcMapperImpl.class);

    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;
    private final DbExecutor<T> executor;
    private final SessionManagerJdbc sessionManagerJdbc;


    public MyJdbcMapperImpl(Class<T> tClass, SessionManagerJdbc sessionManagerJdbc, DbExecutor<T> dbExecutor) {
        this.sessionManagerJdbc = sessionManagerJdbc;
        executor = dbExecutor;
        this.entityClassMetaData = new ReflectionEntityClassMetaData<>(tClass);
        this.entitySQLMetaData = new EntitySqlMetadataImpl<>(entityClassMetaData);
    }

    @Override
    public void insert(Object objectData) {
        String sql = entitySQLMetaData.getInsertSql();
        List<Field> fields = entityClassMetaData.getAllFields();
        List<Object> values = fields.stream()
                .map(field -> ReflectionHelper.getFieldValue(objectData, field))
                .collect(Collectors.toList());

        try {
            long result = executor.executeInsert(sessionManagerJdbc.getCurrentSession().getConnection(), sql, values);
            ReflectionHelper.setFieldValue(objectData, entityClassMetaData.getIdField(), result);
        } catch (SQLException throwables) {
            log.error("Error in INSERT Operation", throwables);
        }


    }


    @Override
    public T findById(Object id, Class<T> clazz) {
        String sql = entitySQLMetaData.getSelectByIdSql();
        Optional<T> optional = Optional.empty();
        try {
            optional = executor.executeSelect(sessionManagerJdbc.getCurrentSession().getConnection(), sql,
                    id, rs -> {
                        try {
                            if (rs.next()) {

                                List<Object> values = entityClassMetaData.getAllFields().stream().map(field -> {
                                    Object value;
                                    try {
                                        value = rs.getObject(field.getName());
                                    } catch (SQLException throwables) {
                                        log.error("Can't find column with name {}", field.getName());
                                        throw new RuntimeException(throwables);
                                    }
                                    return value;
                                }).collect(Collectors.toList());

                                return ReflectionHelper.instantiate(entityClassMetaData.getConstructor(), values.toArray(new Object[values.size()]));

                            }
                        } catch (SQLException e) {
                            log.error(e.getMessage(), e);
                        }
                        return null;
                    });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return optional.orElse(null);
    }
}
