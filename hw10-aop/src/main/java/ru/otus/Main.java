package ru.otus;


/*
Run with -javaagent:build\libs\logger-demo-1.0.jar
 */
public class Main {

    public static void main(String[] args) {
        TestClass testClass = new TestClass();
        testClass.test();
        testClass.helloWorld("Kek");
        testClass.hello("WoW", "LOL");
    }
}
