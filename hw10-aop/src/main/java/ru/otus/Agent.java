package ru.otus;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        for (Class<?> clazz : instrumentation.getAllLoadedClasses()) {
            ClassLoader classLoader = clazz.getClassLoader();
            transform(clazz, classLoader, instrumentation);
        }
    }

    private static void transform(
            Class<?> clazz,
            ClassLoader classLoader,
            Instrumentation instrumentation) {
        LoggerTransformer transformer = new LoggerTransformer();
        instrumentation.addTransformer(transformer);
        try {
            if (instrumentation.isModifiableClass(clazz)) {
                instrumentation.retransformClasses(clazz);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
