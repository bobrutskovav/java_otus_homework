package ru.otus.app.jdbc.mapper;

/**
 * Сохратяет объект в базу, читает объект из базы
 *
 * @param <T>
 */
public interface JdbcMapper<T> {
    void insert(T objectData);

    T findById(Object id, Class<T> clazz);
}
