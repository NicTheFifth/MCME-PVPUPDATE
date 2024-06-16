package com.mcmiddleearth.pvpplugin.runners.runnerUtil;

import com.mcmiddleearth.pvpplugin.runners.gamemodes.TeamConquestRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.TeamDeathmatchRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.TeamSlayerRunner;
import com.mcmiddleearth.pvpplugin.util.Team;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardEditor {
    //<editor-fold defaultstate="collapsed" desc="Team Deathmatch">
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
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Team Slayer">
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
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Team Conquest">

    public static void InitTeamConquest(Scoreboard scoreboard, int scoreGoal) {
        Objective Points = scoreboard.registerNewObjective("Points",
                "dummy", "Points");
        Points.getScore(ChatColor.WHITE + "Goal:").setScore(scoreGoal);
        Points.getScore(ChatColor.BLUE + "Blue:").setScore(0);
        Points.getScore(ChatColor.DARK_RED + "Red:").setScore(0);
        Points.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public static void updateValueTeamConquest(Scoreboard scoreboard, TeamConquestRunner.TCTeam red
            , TeamConquestRunner.TCTeam blue) {
        scoreboard.getObjective("Points")
                .getScore(ChatColor.BLUE + "Blue:").setScore(blue.getPoints());
        scoreboard.getObjective("Points")
                .getScore(ChatColor.DARK_RED + "Red:").setScore(red.getPoints());
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Infected">
    public static void InitInfected(Scoreboard scoreboard, int timeLimitSeconds, Team infected, Team survivors) {
        Objective Points = scoreboard.registerNewObjective("Points",
                "dummy", "Points");
        Points.getScore(ChatColor.WHITE + "Time:").setScore(timeLimitSeconds);
        Points.getScore(ChatColor.DARK_RED + "Infected:").setScore(infected.getOnlineMembers().size());
        Points.getScore(ChatColor.BLUE + "Survivors:").setScore(survivors.getOnlineMembers().size());
        Points.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public static void UpdateTeamsInfected(Scoreboard scoreboard, Team infected, Team survivors){
        scoreboard.getObjective("Points")
                .getScore(ChatColor.DARK_RED + "Infected:").setScore(infected.getOnlineMembers().size());
        scoreboard.getObjective("Points")
                .getScore(ChatColor.BLUE + "Survivors:").setScore(survivors.getOnlineMembers().size());
    }

    public static void UpdateTimeInfected(Scoreboard scoreboard, int timeSeconds){
        scoreboard.getObjective("Points").getScore(ChatColor.WHITE + "Time:").setScore(timeSeconds);
    }
    //</editor-fold>
}
