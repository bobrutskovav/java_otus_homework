package ru.otus.app.jdbc.mapper.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.core.model.annotation.Id;
import ru.otus.app.jdbc.mapper.EntityClassMetaData;
import ru.otus.app.reflection.ReflectionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionEntityClassMetaData<T> implements EntityClassMetaData<T> {

    Logger log = LoggerFactory.getLogger(ReflectionEntityClassMetaData.class);

    private final Class<T> clazz;

    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithOutId;
    private Constructor<T> constructor;

    public ReflectionEntityClassMetaData(Class<T> clazz) {
        this.clazz = clazz;
        allFields = ReflectionHelper.getAllFields(clazz);


        List<Class<?>> allConstructorTypes = allFields.stream().map(Field::getType).collect(Collectors.toList());
        try {
            this.constructor = clazz.getConstructor(allConstructorTypes.toArray(new Class<?>[allConstructorTypes.size()]));
        } catch (NoSuchMethodException e) {
            log.error("Can't find default constructor!", e);
        }
        List<Field> ids = ReflectionHelper.getFieldsWithAnnotation(clazz, Id.class);
        if (ids.size() != 1) {
            throw new RuntimeException("Only one @ID is allowed in Class");
        }
        idField = ids.get(0);

        fieldsWithOutId = allFields.stream()
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
    }

    private void init(Class<T> clazz) {

    }

    @Override
    public String getName() {
        return clazz.getTypeName();
    }

    @Override
    public Constructor<T> getConstructor() {

        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithOutId;
    }
}
