package ru.otus.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonConverter {


    private static final Set<Class<?>> primitiveNumbers = Stream
            .of(int.class, long.class, float.class,
                    double.class, byte.class, short.class)
            .collect(Collectors.toSet());

    private static boolean isNumericType(Class<?> cls) {
        if (cls.isPrimitive()) {
            return primitiveNumbers.contains(cls);
        } else {
            return Number.class.isAssignableFrom(cls);
        }
    }


    public static String getObjectAsJsonString(Object object) {

        Field[] fields = object.getClass().getDeclaredFields();
        return Arrays.stream(fields)
                .map(field -> getStringFromField(object, field))
                .collect(Collectors.joining(",\n", "{\n", "\n}"));

    }

    private static String getStringFromField(Object object, Field field) {
        StringBuilder filedBuilder = new StringBuilder();
        field.setAccessible(true);
        String name = field.getName();
        filedBuilder.append("\"").append(name).append("\"");
        filedBuilder.append(" : ");
        Object currentObject = null;
        try {
            currentObject = field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        filedBuilder.append(objectToString(currentObject));
        return filedBuilder.toString();
    }


    private static String objectToString(Object object) {
        if (object == null) {
            return "null";
        }
        Class clazz = object.getClass();
        if (Collection.class.isAssignableFrom(clazz)) {
            object = ((Collection) object).toArray();
        }
        if (object.getClass().isArray()) {
            Object[] values = new Object[Array.getLength(object)];
            for (int i = 0; i < values.length; i++)
                values[i] = Array.get(object, i);
            return Arrays.stream(values)
                    .map(value -> objectToString(value))
                    .collect(Collectors.joining(",", "[ ", " ]"));

        } else {
            if (clazz.isAssignableFrom(String.class)) {
                String value = object.toString();
                return String.format("\"%s\"", value);
            } else if (isNumericType(clazz) || clazz.equals(Boolean.class)) {
                return object.toString();
            } else {
                throw new IllegalArgumentException("Can't Field with type " + clazz.getName());
            }
        }

    }
}
