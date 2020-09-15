package ru.otus.json;

import java.util.List;

public class HelloTest {

    private Object isNullHere;

    private String string;

    private int number;

    private boolean isBool = true;

    private boolean[] isMassiveBool = {false, true};

    private int[] intArray = {1, 2, 3};

    private String[] stringArray;

    private List<String> collection;

    private List<String[]> listOfArrays;

    public HelloTest(String string, int number, String[] stringArray, List<String> collection, List<String[]> listOfArrays) {
        this.string = string;
        this.number = number;
        this.stringArray = stringArray;
        this.collection = collection;
        this.listOfArrays = listOfArrays;
    }
}
