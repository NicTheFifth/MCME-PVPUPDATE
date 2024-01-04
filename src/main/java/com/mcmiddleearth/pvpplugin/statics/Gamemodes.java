package com.mcmiddleearth.pvpplugin.statics;

import java.util.HashSet;
import java.util.Set;

public class Gamemodes {

    public static final String CAPTURETHEFLAG;
    public static final String DEATHRUN;
    public static final String INFECTED;
    public static final String TEAMCONQUEST;
    public static final String TEAMDEATHMATCH;
    public static final String TEAMSLAYER;
    public static final String ONEINTHEQUIVER;
    public static final String RINGBEARER;

    static{
        CAPTURETHEFLAG= "capturetheflag";
        DEATHRUN = "deathrun";
        INFECTED = "infected";
        TEAMCONQUEST = "teamconquest";
        TEAMDEATHMATCH = "teamdeathmatch";
        TEAMSLAYER = "teamslayer";
        ONEINTHEQUIVER = "oneinthequiver";
        RINGBEARER = "ringbearer";
    }

    public static final HashSet<String> getAll =
        new HashSet<>(Set.of(CAPTURETHEFLAG,DEATHRUN,INFECTED,TEAMCONQUEST,
            TEAMDEATHMATCH,TEAMSLAYER, ONEINTHEQUIVER,RINGBEARER));
}
