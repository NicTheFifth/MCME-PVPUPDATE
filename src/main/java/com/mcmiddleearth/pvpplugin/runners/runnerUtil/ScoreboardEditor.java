package com.mcmiddleearth.pvpplugin.runners.runnerUtil;

import com.mcmiddleearth.pvpplugin.runners.gamemodes.TeamDeathmatchRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.TeamSlayerRunner;
import com.mcmiddleearth.pvpplugin.util.Team;
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
    public static void updateTime(Scoreboard scoreboard, Integer time){
        int timeMin = time / 60;
        int timeSec = time % 60;
        scoreboard.getObjective("Remaining").setDisplayName("Time: " + timeMin + "m " + timeSec + "s");
    }
    public static void updateValueInfected(Scoreboard scoreboard, Team infected, Team survivors){
        scoreboard.getObjective("Remaining").getScore(ChatColor.BLUE + "Survivors:").setScore(survivors.getMembers().size());
        scoreboard.getObjective("Remaining").getScore(ChatColor.DARK_RED + "Infected:").setScore(infected.getMembers().size());
    }
    public static void InitTeamDeathmatch(Scoreboard scoreboard,
                                          TeamDeathmatchRunner.TDMTeam red,
                                          TeamDeathmatchRunner.TDMTeam blue){
        Objective Points = scoreboard.registerNewObjective("Remaining",
            "dummy", "Remaining");
        Points.getScore(ChatColor.BLUE + "Blue:").setScore(blue.AliveMembers());
        Points.getScore(ChatColor.DARK_RED + "Red:").setScore(red.AliveMembers());
        Points.setDisplaySlot(DisplaySlot.SIDEBAR);
    }
    public static void updateValueTeamDeathmatch(Scoreboard scoreboard,
                                                 TeamDeathmatchRunner.TDMTeam red,
                                                 TeamDeathmatchRunner.TDMTeam blue){
        scoreboard.getObjective("Remaining").getScore(
            ChatColor.BLUE + "Blue:").setScore(blue.AliveMembers());
        scoreboard.getObjective("Remaining").getScore(
            ChatColor.DARK_RED + "Red:").setScore(red.AliveMembers());
    }

    public static void InitTeamSlayer(Scoreboard scoreboard, Integer scoreGoal) {
        Objective Points = scoreboard.registerNewObjective("Points",
            "dummy", "Points");
        Points.getScore(ChatColor.WHITE + "Goal:").setScore(scoreGoal);
        Points.getScore(ChatColor.BLUE + "Blue:").setScore(0);
        Points.getScore(ChatColor.DARK_RED + "Red:").setScore(0);
        Points.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public static void updateValueTeamSlayer(Scoreboard scoreboard, TeamSlayerRunner.TSTeam red
        , TeamSlayerRunner.TSTeam blue) {
        scoreboard.getObjective("Points")
            .getScore(ChatColor.BLUE + "Blue:").setScore(blue.getPoints());
        scoreboard.getObjective("Points")
            .getScore(ChatColor.DARK_RED + "Red:").setScore(red.getPoints());
    }
}
