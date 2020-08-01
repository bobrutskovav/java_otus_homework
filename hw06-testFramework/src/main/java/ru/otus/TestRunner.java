package ru.otus;

import ru.otus.annotation.AfterTest;
import ru.otus.annotation.BeforeTest;
import ru.otus.annotation.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static ru.otus.ReflectionHelper.getMethodsWithAnnotation;

public class TestRunner {


    public static void runTest(Class testClass) {


        List<CustomTest> failedTests = new ArrayList<>();
        List<CustomTest> tests = new ArrayList<>();

        if (testClass.isAnnotationPresent(Test.class)) {

            for (Method declaredMethod : testClass.getDeclaredMethods()) {
                if (declaredMethod.isAnnotationPresent(Test.class)) {
                    CustomTest test = new CustomTest(declaredMethod);
                    tests.add(test);
                }
            }

            List<Method> beforeMethods = getMethodsWithAnnotation(testClass, BeforeTest.class);

            List<Method> afterMethods = getMethodsWithAnnotation(testClass, AfterTest.class);


            tests.forEach(test -> {
                test.setBeforeActions(beforeMethods);
                test.setAfterActions(afterMethods);
            });

            tests.parallelStream().forEach(test -> {
                Object testObject = ReflectionHelper.instantiate(testClass);

                try {
                    test.getBeforeActions().forEach(before -> {
                        ReflectionHelper.invokeMethod(testObject, before);
                    });

                    ReflectionHelper.invokeMethod(testObject, test.testMethod);

                    test.getAfterActions().forEach(after -> {
                        ReflectionHelper.invokeMethod(testObject, after);
                    });
                } catch (Throwable throwable) {
                    test.setError(throwable);
                    failedTests.add(test);
                }
            });

            System.out.println(String.format("Total tests runs : %d\nTests Failed : %d", tests.size(), failedTests.size()));
            if (!failedTests.isEmpty()) {

                System.out.println("----FAILED TESTS----");
                failedTests.forEach(test -> {
                    System.out.println("Failed " + test.testMethod.getName());
                    System.out.println("Reason : \n");
                    test.getError().printStackTrace();
                });
            }

        }
    }


    private static class CustomTest {
        private Method testMethod;
        private List<Method> beforeActions;
        private List<Method> afterActions;
        private Throwable error;

        public CustomTest(Method testMethod) {
            this.testMethod = testMethod;
        }


        public List<Method> getBeforeActions() {
            return beforeActions;
        }

        public void setBeforeActions(List<Method> beforeActions) {
            this.beforeActions = beforeActions;
        }

        public List<Method> getAfterActions() {
            return afterActions;
        }

        public void setAfterActions(List<Method> afterActions) {
            this.afterActions = afterActions;
        }

        public Throwable getError() {
            return error;
        }

        public void setError(Throwable error) {
            this.error = error;
        }
    }
}
