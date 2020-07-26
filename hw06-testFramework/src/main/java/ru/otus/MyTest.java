package ru.otus;


import ru.otus.annotation.AfterTest;
import ru.otus.annotation.BeforeTest;
import ru.otus.annotation.Test;

import java.util.ArrayList;

@Test
public class MyTest {

    ArrayList<String> strings = new ArrayList<>();

    @BeforeTest
    public void before() {
        System.out.println("Before Here! " + Thread.currentThread().getName());
        strings.add("Kek");
    }


    @Test
    public void testTwo() {
        System.out.println("Test Two Here! " + Thread.currentThread().getName());
        strings.add("Lol2");
    }


    @Test
    public void testOne() {
        System.out.println("Test One Here! " + Thread.currentThread().getName());
        strings.add("Lol1");
    }

    @Test
    public void testThree() {
        throw new AssertionError("WOW!");
    }


    @AfterTest
    public void after() {
        System.out.println("After Here! " + Thread.currentThread().getName());
        System.out.println(strings);
    }
}
