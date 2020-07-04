package ru.otus;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Application {
    public static void main(String[] args) {
        BiMap<String, String> biMap = HashBiMap.create();
        biMap.put("Kek", "LOL");
        biMap.put("ROFL", "KeK");
        System.out.println(biMap.inverse().get("LOL"));
        System.out.println(biMap.get("ROFL"));
    }
}
