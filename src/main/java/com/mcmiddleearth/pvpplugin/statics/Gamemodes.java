package com.mcmiddleearth.pvpplugin.statics;

import java.util.HashSet;
import java.util.Set;

public class Gamemodes {

    public static final String CAPTURETHEFLAG = "capturetheflag";
    public static final String DEATHRUN = "deathrun";
    public static final String FREEFORALL = "freeforall";
    public static final String INFECTED = "infected";
    public static final String TEAMCONQUEST = "teamconquest";
    public static final String TEAMDEATHMATCH = "teamdeathmatch";
    public static final String TEAMSLAYER = "teamslayer";
    public static final String ONEINTHEQUIVER = "oneinthequiver";
    public static final String RINGBEARER = "ringbearer";

    public static final HashSet<String> getAll =
        new HashSet<>(Set.of(CAPTURETHEFLAG,DEATHRUN,FREEFORALL,INFECTED,
            TEAMCONQUEST,TEAMDEATHMATCH,TEAMSLAYER, ONEINTHEQUIVER,RINGBEARER));
}
