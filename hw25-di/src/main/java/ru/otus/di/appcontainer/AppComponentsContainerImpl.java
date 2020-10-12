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
        configClasses.stream()
                .sorted(Comparator.comparingInt(config -> config.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);

    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        Arrays.stream(initialConfigClasses)
                .sorted(Comparator.comparingInt(config -> config.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object configObject;
        try {
            configObject = configClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Can't init configuration class!");
        }

        List<Method> appComponentMethods = Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .collect(Collectors.toList());

        // You code here...

        appComponentMethods.stream()
                .sorted(Comparator.comparingInt(config -> config.getAnnotation(AppComponent.class).order()))
                .forEach(method -> {
                    String componentName = method.getAnnotation(AppComponent.class).name();
                    if (method.getParameterCount() == 0) {
                        initNoDependencyComponent(configObject, method, componentName);
                    } else {
                        initComponentWithDependency(configObject, method, componentName);
                    }
                });
    }

    private void initComponentWithDependency(Object configObject, Method method, String componentName) {
        List<Class> parameterTypes = Arrays.stream(method.getParameters())
                .map(Parameter::getType)
                .collect(Collectors.toList());
        List<Object> readyComponents = parameterTypes.stream().map(type -> appComponents.stream()
                .filter(type::isInstance)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find ready component with class " + type.getName()))).collect(Collectors.toList());

        Object[] parameters = readyComponents.toArray();
        try {
            Object component = method.invoke(configObject, parameters);
            appComponentsByName.put(componentName, component);
            appComponents.add(component);
        } catch (Exception e) {
            throw new RuntimeException("Can't init AppComponent " + componentName, e);
        }
    }

    private void initNoDependencyComponent(Object configObject, Method method, String componentName) {
        try {
            Object component = method.invoke(configObject);
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

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                .filter(component -> componentClass.isInstance(component))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find component for this class " + componentClass.getName()));
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return null;
    }
}
