package ru.otus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;


public class DiyArrayListTest {

    private List<String> list;

    @Before
    public void initList() {
        list = new DiyArrayList<>();
        for (int i = 30; i > 0; i--) {
            list.add(String.valueOf(i));
        }

    }

    @Test
    public void sortTest() {

        Collections.sort(list);
        Assert.assertEquals("9", list.get(29));
    }


    @Test
    public void addAllTest() {
        String element = "another";
        List<String> list2 = new DiyArrayList<>();
        list2.add(element);
        list.addAll(list2);
        Assert.assertEquals(element, list.get(10));
    }

    @Test
    public void copyTest() {

//Этот пример так же не работает на стандартной реализации ArrayList
//        List<String> src = new ArrayList<>();
//        for (int i = 30; i > 0; i--) {
//            src.add(String.valueOf(i));
//        }
//
//        List<String> dest = new ArrayList<>(src.size() + 100);
//        Collections.copy(dest,src);

        List<String> copied = new DiyArrayList<>(list.size());

        for (int i = 30; i > 0; i--) {
            copied.add(null);
        }
        Collections.copy(copied, list);
        Assert.assertEquals(copied.size(), list.size());


    }
}
