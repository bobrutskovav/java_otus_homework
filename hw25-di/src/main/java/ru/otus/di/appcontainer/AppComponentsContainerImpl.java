package ru.otus.di.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import ru.otus.di.appcontainer.api.AppComponent;
import ru.otus.di.appcontainer.api.AppComponentsContainer;
import ru.otus.di.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }


    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(packageName, new TypeAnnotationsScanner(), new SubTypesScanner());
        Set<Class<?>> configClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);
        if (configClasses.isEmpty()) {
            throw new RuntimeException("Can't find any configuration class in package " + packageName);
        }
        processStreamWithConfigClasses(configClasses.stream());

    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        processStreamWithConfigClasses(Arrays.stream(initialConfigClasses));
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object configObject = initConfigObject(configClass);
        List<Method> appComponentMethods = Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .collect(Collectors.toList());

        // You code here...

        appComponentMethods.stream()
                .sorted(Comparator.comparingInt(config -> config.getAnnotation(AppComponent.class).order()))
                .forEach(method -> {
                    String componentName = method.getAnnotation(AppComponent.class).name();
                    initComponent(configObject, method, componentName);
                });
    }

    private void initComponent(Object configObject, Method method, String componentName) {
        Object[] parameters;
        List<Class<?>> parameterTypes = Arrays.stream(method.getParameters())
                .map(Parameter::getType)
                .collect(Collectors.toList());
        if (parameterTypes.isEmpty()) {
            parameters = new Object[0];
        } else {
            parameters = parameterTypes.stream()
                    .map(this::getAppComponent).toArray();
        }

        try {
            Object component = method.invoke(configObject, parameters);
            appComponentsByName.put(componentName, component);
            appComponents.add(component);
        } catch (Exception e) {
            throw new RuntimeException("Can't init AppComponent " + componentName, e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private void processStreamWithConfigClasses(Stream<Class<?>> configStream) {
        configStream
                .sorted(Comparator.comparingInt(config -> config.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);
    }

    private Object initConfigObject(Class<?> configClass) {
        try {
            return configClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Can't init configuration class!");
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                .filter(component -> componentClass.isInstance(component))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find component for this class " + componentClass.getName()));
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            return (C) appComponentsByName.get(componentName);
        } else {
            throw new RuntimeException("Can't find component for this class " + componentName);
        }
    }
}
