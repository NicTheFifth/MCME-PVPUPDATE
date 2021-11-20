package com.mcmiddleearth.mcme.pvpplugin.runners.runnerUtil;

import com.mcmiddleearth.mcme.pvpplugin.util.Team;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardEditor {
    public static void InitInfected(Scoreboard scoreboard, Team infected, Team survivors, Integer time){
        int timeMin = time / 60;
        int timeSec = time % 60;
        Objective Points = scoreboard.registerNewObjective("Remaining", "dummy", "Time: " + timeMin + "m " + timeSec + "s");
        Points.getScore(ChatColor.BLUE + "Survivors:").setScore(survivors.getMembers().size());
        Points.getScore(ChatColor.DARK_RED + "Infected:").setScore(infected.getMembers().size());
        Points.setDisplaySlot(DisplaySlot.SIDEBAR);
    }
    public static void InitTeamDeathmatch(Scoreboard scoreboard, Team red, Team blue){
        Objective Points = scoreboard.registerNewObjective("Remaining", "dummy");
        Points.getScore(ChatColor.BLUE + "Blue:").setScore(blue.getMembers().size());
        Points.getScore(ChatColor.DARK_RED + "Red:").setScore(red.getMembers().size());
        Points.setDisplaySlot(DisplaySlot.SIDEBAR);
    }
    public static void updateTime(Scoreboard scoreboard, Integer time){
        int timeMin = time / 60;
        int timeSec = time % 60;
        scoreboard.getObjective("Remaining").setDisplayName("Time: " + timeMin + "m " + timeSec + "s");
    }
    public static void updateValueInfected(Scoreboard scoreboard, Team infected, Team survivors){
        scoreboard.getObjective("Remaining").getScore(ChatColor.BLUE + "Survivors:").setScore(survivors.getMembers().size());
        scoreboard.getObjective("Remaining").getScore(ChatColor.DARK_RED + "Infected:").setScore(infected.getMembers().size());
    }
    public static void updateValueTeamDeathmatch(Scoreboard scoreboard, Team red, Team blue){
        scoreboard.getObjective("Remaining").getScore(ChatColor.BLUE + "Blue:").setScore(blue.getMembers().size());
        scoreboard.getObjective("Remaining").getScore(ChatColor.DARK_RED + "Red:").setScore(red.getMembers().size());
    }
}
