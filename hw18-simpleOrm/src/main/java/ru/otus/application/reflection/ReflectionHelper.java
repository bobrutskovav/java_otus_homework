package ru.otus.application.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class ReflectionHelper {
    private ReflectionHelper() {
    }

    public static Object getFieldValue(Object object, String name) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Object getFieldValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void setFieldValue(Object object, String name, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void setFieldValue(Object object, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<Field> getFieldsWithAnnotation(Class clazz, Class<? extends Annotation> annotation) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    public static Object callMethod(Object object, String name, Object... args) {
        try {
            Method method = object.getClass().getDeclaredMethod(name, toClasses(args));
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Method> getMethodsWithAnnotation(Class testClass, Class<? extends Annotation> annotation) {
        return Arrays.stream(testClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    public static Object invokeMethod(Object object, Method method, Object... args) {

        try {
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.getDeclaredConstructor().newInstance();
            } else {
                Class<?>[] classes = toClasses(args);
                return type.getDeclaredConstructor(classes).newInstance(args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> T instantiate(Constructor<T> constructor, Object... args) {
        try {
            return constructor.newInstance(args);

        } catch (IllegalAccessException | InstantiationException | InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }


    public static List<Field> getAllFields(Class clazz) {
        return Arrays.asList(clazz.getDeclaredFields());
    }
}
