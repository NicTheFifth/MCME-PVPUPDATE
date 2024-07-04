package com.mcmiddleearth.pvpplugin.statics;

import com.mcmiddleearth.pvpplugin.runners.gamemodes.*;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.util.HashMapFactory;
import com.mcmiddleearth.pvpplugin.util.HashSetFactory;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.HashMap;
import java.util.HashSet;

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

    public static final HashMap<String, Class<? extends GamemodeRunner>> getRunners =
            new HashMapFactory<String, Class<? extends GamemodeRunner>>()
                    .put(CAPTURETHEFLAG, CaptureTheFlagRunner.class)
                    .put(DEATHRUN, DeathRunRunner.class)
                    .put(INFECTED, InfectedRunner.class)
                    .put(ONEINTHEQUIVER, OneInTheQuiverRunner.class)
                    .put(RINGBEARER, RingBearerRunner.class)
                    .put(TEAMCONQUEST, TeamConquestRunner.class)
                    .put(TEAMDEATHMATCH, TeamDeathmatchRunner.class)
                    .put(TEAMSLAYER, TeamSlayerRunner.class)
                    .build();

    public static final HashMap<String, BaseComponent[]> getRules =
        new HashMapFactory<String, BaseComponent[]>()
            .put(CAPTURETHEFLAG, new ComponentBuilder("Capture the Flag rules").bold(true)
                    .append(new ComponentBuilder("\nCapture the enemy flag(banner), by right clicking it, and escort it to your base beacon while protecting your own. " +
                            "To score a point, run towards your own flag whilst carrying the flag, once you get close enough, you'll score.").bold(false).create())
                    .append("\n\nTry to reach the score goal before time is up, or else there might be a sudden death!").create())
            .put(DEATHRUN,  new ComponentBuilder("Death Run rules").bold(true)
                    .append(new ComponentBuilder("\nOne death, and lots of runners. Runners have to reach the end goal before the time limit is reached or they're killed by death.").bold(false).create()).create())
            .put(FREEFORALL, new ComponentBuilder("Free for All Rules").bold(true)
                    .append(new ComponentBuilder("\nEvery man for himself, madly killing everyone! Get as many kills before the time limit is reached.").bold(false).create()).create())
            .put(INFECTED, new ComponentBuilder("Infected rules").bold(true)
                    .append(new ComponentBuilder("\nInfected versus survivors. Can you survive until the time limit is reached, or will you meet your demise and join the Infected? " +
                            "Survivors aim to stay alive until the time limit is reached, whilst the infected aim to kill all survivors before the time limit is reached.").bold(false).create()).create())
            .put(ONEINTHEQUIVER, new ComponentBuilder("One in the Quiver rules").bold(true)
                    .append(new ComponentBuilder("\nEveryone gets an axe, a bow, and one arrow. Arrows instakill when hit. " +
                            "When you kill, you get a new arrow. Try to reach the score goal before anyone else!").bold(false).create()).create())
            .put(RINGBEARER, new ComponentBuilder("Ringbearer rules").bold(true)
                    .append(new ComponentBuilder("\nIn this team versus team game, there exists one ringbearer per team. " +
                            "Kill your enemy's ringbearer to stop their respawning. Last team standing wins.").bold(false).create()).create())
            .put(TEAMCONQUEST, new ComponentBuilder("Team Conquest rules").bold(true)
                    .append(new ComponentBuilder("\nIn this team versus team game, you have to capture points, before killing enemies to score. " +
                            "The points gained is the positive difference you have with the enemy team in captured points. " +
                            "Try to reach the score goal before the enemy team!").bold(false).create()).create())
            .put(TEAMDEATHMATCH, new ComponentBuilder("Team Deathmatch rules").bold(true)
                    .append(new ComponentBuilder("\nIn this team versus team game, it's easy, kill the enemy team before they kill your team!").bold(false).create()).create())
            .put(TEAMSLAYER, new ComponentBuilder("Team Slayer rules").bold(true)
                    .append(new ComponentBuilder("\nIn this team versus team game, try to reach the score goal by killing enemy team members. " +
                            "Each kill is worth one point.").bold(false).create()).create())
            .build();
}
