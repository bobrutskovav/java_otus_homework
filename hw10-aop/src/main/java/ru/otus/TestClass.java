package ru.otus;

public class TestClass {


    @Log
    public void test() {
        System.out.println("Test");
    }


    @Log
    public void helloWorld(String worldName) {
        System.out.println("Hello " + worldName);
    }


    @Log
    public void hello(String helloWord, String worldName) {
        System.out.println(helloWord + " " + worldName);
    }

}
