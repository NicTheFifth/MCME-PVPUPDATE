package com.mcmiddleearth.pvpplugin.runners.runnerUtil;


import com.mcmiddleearth.pvpplugin.runners.gamemodes.OneInTheQuiverRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.TeamConquestRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.TeamDeathmatchRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.TeamSlayerRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.FreeForAllRunner;

import com.mcmiddleearth.pvpplugin.runners.gamemodes.*;

import com.mcmiddleearth.pvpplugin.util.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Map;

public class ScoreboardEditor {
    //<editor-fold defaultstate="collapsed" desc="Team Deathmatch">
    public static void InitTeamDeathmatch(Scoreboard scoreboard,
                                          TeamDeathmatchRunner.TDMTeam red,
                                          TeamDeathmatchRunner.TDMTeam blue){
        Objective Points = scoreboard.registerNewObjective("Remaining",
                Criteria.DUMMY, "Remaining");
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
                Criteria.DUMMY, "Points");
        Points.getScore(ChatColor.WHITE + "Goal:").setScore(scoreGoal);
        Points.getScore(ChatColor.BLUE + "Blue:").setScore(0);
        Points.getScore(ChatColor.RED + "Red:").setScore(0);
        Points.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public static void updateValueTeamSlayer(Scoreboard scoreboard, TeamSlayerRunner.TSTeam red
        , TeamSlayerRunner.TSTeam blue) {
        scoreboard.getObjective("Points")
            .getScore(ChatColor.BLUE + "Blue:").setScore(blue.getPoints());
        scoreboard.getObjective("Points")
            .getScore(ChatColor.RED + "Red:").setScore(red.getPoints());
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Team Conquest">
    public static void InitTeamConquest(Scoreboard scoreboard, int scoreGoal) {
        Objective Points = scoreboard.registerNewObjective("Points",
                Criteria.DUMMY, "Points");
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
                Criteria.DUMMY, "Points");
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
    //<editor-fold defaultstate="collapsed" desc="One in the Quiver">
    public static void InitOneInTheQuiver(Scoreboard scoreboard, Map<Player, OneInTheQuiverRunner.PlayerTeam> players, int scoreGoal) {
        Objective Points = scoreboard.registerNewObjective("Points",
                Criteria.DUMMY, "Points");
        Points.getScore(ChatColor.WHITE + "Goal:").setScore(scoreGoal);
        players.forEach((player, playerTeam) ->
                Points.getScore(playerTeam.getChatColor() + player.getName() + ":").setScore(0));
    }
    public static void UpdateOneInTheQuiver(Scoreboard scoreboard, Player player, OneInTheQuiverRunner.PlayerTeam playerTeam){
        scoreboard.getObjective("Points")
                .getScore(playerTeam.getChatColor() + player.getName() + ":").setScore(playerTeam.getKills());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Free for All">
    public static void InitFreeForAll(Scoreboard scoreboard, Map<Player, FreeForAllRunner.PlayerTeam> players, int timeLimitSeconds) {
        Objective Points = scoreboard.registerNewObjective("Points",
                "dummy", "Points");
        Points.getScore(ChatColor.WHITE + "Time:").setScore(timeLimitSeconds);
        players.forEach((player, playerTeam) ->
                Points.getScore(playerTeam.getChatColor() + player.getName() + ":").setScore(0));
    }
    public static void UpdateFreeForAll(Scoreboard scoreboard, Player player, FreeForAllRunner.PlayerTeam playerTeam){
        scoreboard.getObjective("Points")
                .getScore(playerTeam.getChatColor() + player.getName() + ":").setScore(playerTeam.getKills());
    }
    public static void UpdateTimeFreeForAll(Scoreboard scoreboard, int timeSeconds){
        scoreboard.getObjective("Points").getScore(ChatColor.WHITE + "Time:").setScore(timeSeconds);
    }
    //</editor-fold>

    public static void InitRingBearer(Scoreboard scoreboard, RingBearerRunner.RBTeam redTeam, RingBearerRunner.RBTeam blueTeam) {
        Objective Points = scoreboard.registerNewObjective("Remaining", Criteria.DUMMY, "Remaining");
        Points.getScore(ChatColor.BLUE + "Blue:").setScore(blueTeam.AliveMembers());
        Points.getScore(ChatColor.DARK_RED + "Red:").setScore(redTeam.AliveMembers());
        Points.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public static void UpdateRingBearer(Scoreboard scoreboard, RingBearerRunner.RBTeam redTeam, RingBearerRunner.RBTeam blueTeam) {
        scoreboard.getObjective("Remaining").getScore(ChatColor.BLUE + "Blue:").setScore(blueTeam.AliveMembers());
        scoreboard.getObjective("Remaining").getScore(ChatColor.DARK_RED + "Red:").setScore(redTeam.AliveMembers());
    }

    public static void InitDeathRun(Scoreboard scoreboard, int timeLimitSeconds, DeathRunRunner.DRTeam runner) {
        Objective Points = scoreboard.registerNewObjective("Points",
                Criteria.DUMMY, "Points");
        Points.getScore(ChatColor.WHITE + "Time:").setScore(timeLimitSeconds);
        Points.getScore(runner.getChatColor() + "Runners:").setScore(runner.getOnlineMembers().size());
        Points.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public static void UpdateTimeDeathRun(Scoreboard scoreboard, int timeSeconds) {
        scoreboard.getObjective("Points").getScore(ChatColor.WHITE + "Time:").setScore(timeSeconds);
    }

    public static void UpdateRunnersDeathRun(Scoreboard scoreboard, DeathRunRunner.DRTeam runner){
        scoreboard.getObjective("Points").getScore(runner.getChatColor() + "Runners:").setScore(runner.getOnlineMembers().size());
    }

    public static void InitCaptureTheFlag(Scoreboard scoreboard, int scoreGoal, int timeLimit) {
        Objective Points = scoreboard.registerNewObjective("Points",
                Criteria.DUMMY, "Points");
        Points.getScore(ChatColor.WHITE + "Time:").setScore(timeLimit);
        Points.getScore(ChatColor.WHITE + "Goal:").setScore(scoreGoal);
        Points.getScore(ChatColor.BLUE + "Blue:").setScore(0);
        Points.getScore(ChatColor.RED + "Red:").setScore(0);
        Points.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public static void UpdateTimeCaptureTheFlag(Scoreboard scoreboard, int timeLimit){
        scoreboard.getObjective("Points").getScore(ChatColor.WHITE + "Time:").setScore(timeLimit);
    }

    public static void UpdatePointsCaptureTheFlag(Scoreboard scoreboard, CaptureTheFlagRunner.CTFTeam blueTeam, CaptureTheFlagRunner.CTFTeam redTeam) {
        scoreboard.getObjective("Points").getScore(ChatColor.BLUE + "Blue:").setScore(blueTeam.getPoints());
        scoreboard.getObjective("Points").getScore(ChatColor.RED + "Red:").setScore(redTeam.getPoints());
    }

}
