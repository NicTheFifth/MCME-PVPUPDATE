package com.mcmiddleearth.pvpplugin.statics;

import com.mcmiddleearth.pvpplugin.util.HashMapFactory;
import com.mcmiddleearth.pvpplugin.util.HashSetFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Gamemodes {

    public static final String CAPTURETHEFLAG = "capturetheflag";
    public static final String DEATHRUN = "deathrun";
    public static final String FREEFORALL = "freeforall";
    public static final String INFECTED = "infected";
    public static final String ONEINTHEQUIVER = "oneinthequiver";
    public static final String RINGBEARER = "ringbearer";
    public static final String TEAMCONQUEST = "teamconquest";
    public static final String TEAMDEATHMATCH = "teamdeathmatch";
    public static final String TEAMSLAYER = "teamslayer";

    public static final HashSet<String> getAll =
        new HashSetFactory<String>()
            .add(CAPTURETHEFLAG)
            .add(DEATHRUN)
            .add(FREEFORALL)
            .add(INFECTED)
            .add(ONEINTHEQUIVER)
            .add(RINGBEARER)
            .add(TEAMCONQUEST)
            .add(TEAMDEATHMATCH)
            .add(TEAMSLAYER)
            .build();

    public static final HashMap<String, String> getRules =
        new HashMapFactory<String, String>()
            .put(CAPTURETHEFLAG, "Capture the Flag rules.")
            .put(DEATHRUN, "Death Run rules.")
            .build();
}
