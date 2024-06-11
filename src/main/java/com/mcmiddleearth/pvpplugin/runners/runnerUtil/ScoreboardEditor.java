package com.mcmiddleearth.pvpplugin.runners.runnerUtil;

import com.mcmiddleearth.pvpplugin.runners.gamemodes.TeamConquest;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.TeamDeathmatchRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.TeamSlayerRunner;
import com.mcmiddleearth.pvpplugin.util.Team;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardEditor {
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

    public static void InitTeamConquest(Scoreboard scoreboard, int scoreGoal) {
        Objective Points = scoreboard.registerNewObjective("Points",
                "dummy", "Points");
        Points.getScore(ChatColor.WHITE + "Goal:").setScore(scoreGoal);
        Points.getScore(ChatColor.BLUE + "Blue:").setScore(0);
        Points.getScore(ChatColor.DARK_RED + "Red:").setScore(0);
        Points.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public static void updateValueTeamConquest(Scoreboard scoreboard, TeamConquest.TCTeam red
            , TeamConquest.TCTeam blue) {
        scoreboard.getObjective("Points")
                .getScore(ChatColor.BLUE + "Blue:").setScore(blue.getPoints());
        scoreboard.getObjective("Points")
                .getScore(ChatColor.DARK_RED + "Red:").setScore(red.getPoints());
    }
}
