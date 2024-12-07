package common;

import java.util.HashMap;
import java.util.HashSet;

public class HashMapWithSet {
    private final HashMap<Integer, HashSet<Integer>> hashMap;

    public HashMapWithSet() {
        hashMap = new HashMap<>();
    }

    public void put(int key, int value) {
        hashMap.computeIfAbsent(key, k -> new HashSet<>()).add(value);
    }

    public HashSet<Integer> get(int key) {
        return hashMap.getOrDefault(key, new HashSet<>());
    }

    public HashMap<Integer, HashSet<Integer>> getHashMap() {
        return hashMap;
    }
}
