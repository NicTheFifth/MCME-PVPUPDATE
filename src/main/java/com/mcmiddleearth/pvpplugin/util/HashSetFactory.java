package com.mcmiddleearth.pvpplugin.util;

import java.util.HashSet;

public class HashSetFactory<T> {
    private final HashSet<T> hashSet;
    public HashSetFactory(){
        hashSet = new HashSet<>();
    }

    public HashSetFactory<T> add(T e){
        hashSet.add(e);
        return this;
    }

    public HashSet<T> build() {
        return hashSet;
    }
}
