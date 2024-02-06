package com.mcmiddleearth.pvpplugin.util;

import java.util.HashMap;

public class HashMapFactory<K,V> {
    private final HashMap<K,V> hashMap;
    public HashMapFactory(){
        hashMap = new HashMap<>();
    }
    public HashMapFactory<K,V> put(K key, V value){
        hashMap.put(key, value);
        return this;
    }
    public HashMap<K,V> build(){
        return this.hashMap;
    }
}
