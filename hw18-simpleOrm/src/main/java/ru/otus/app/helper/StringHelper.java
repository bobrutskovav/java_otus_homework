package ru.otus.app.helper;

public class StringHelper {

    private StringHelper() {
    }

    public static String stripPackageName(String string) {
        return string.substring(string.lastIndexOf(".") + 1);
    }
}
