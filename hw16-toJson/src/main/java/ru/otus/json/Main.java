package ru.otus.json;

import java.util.Arrays;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IllegalAccessException {
        HelloTest helloTest = new HelloTest("KekString",
                123,
                new String[]{"ArrayStr1", "ArrayStr2"},
                Arrays.asList("collectionStr1", "collectionStr2"),
                List.of(new String[]{"Array1InCollectionStr1", "Array1InCollectionStr1"}, new String[]{"Array2InCollectionStr1", "Array2InCollectionStr1"}));


        String json = JsonConverter.getObjectAsJsonString(helloTest);
        System.out.println(json);
    }
}
