package com.mcmiddleearth.mcme.pvpplugin.util;

import org.bukkit.ChatColor;

public enum Gamemodes {
    FreeForAll (ChatColor.GREEN + "Free For All Rules",
            ChatColor.GRAY + "Every man for himself, madly killing everyone! Highest number of kills wins."),
    Infected ( ChatColor.GREEN + "Infected Rules",
            ChatColor.GRAY + "Everyone starts as a Survivor, except one person, who is Infected. Infected gets a Speed effect, but has less armor.",
            ChatColor.GRAY + "If a Survivor is killed, they become Infected. Infected players have infinite respawns.",
            ChatColor.GRAY + "If all Survivors are infected, Infected team wins. If the time runs out with Survivors remaining, Survivors win."),
    OneInTheQuiver (ChatColor.GREEN + "One in the Quiver Rules",
                  ChatColor.GRAY + "Everyone gets an axe, a bow, and one arrow, which kills in 1 shot if the bow is fully drawn.",
                  ChatColor.GRAY + "Every man is fighting for himself. If they get a kill or die, they get another arrow, up to a max of 5 arrows",
                  ChatColor.GRAY + "First to 21 kills wins."),
    Ringbearer (ChatColor.GREEN + "Ringbearer Rules",
                ChatColor.GRAY + "Two teams, each with a ringbearer, who gets The One Ring (which of course gives invisibility)",
                ChatColor.GRAY + "As long as the ringbearer is alive, the team can respawn.",
                ChatColor.GRAY + "Once the ringbearer dies, that team cannot respawn. The first team to run out of members loses."),
    TeamConquest (ChatColor.GREEN + "Team Conquest Rules",
                  ChatColor.GRAY + "Two teams. There are 3 beacons, which each team can capture by repeatedly right clicking the beacon.",
                  ChatColor.GRAY + "Points are awarded on kills, based on the difference between each team's number of beacons.",
                  ChatColor.GRAY + "i.e. if Red has 3 beacons and Blue has 0, Red gets 3 point per kill. If Red has 1 and Blue has 2, Red doesn't get points for a kill.",
                  ChatColor.GRAY + "First team to a certain point total wins."),
    TeamDeathmatch (ChatColor.GREEN + "Team Deathmatch Rules",
                   ChatColor.GRAY + "Two teams, and no respawns. First team to run out of players loses."),
    TeamSlayer (ChatColor.GREEN + "Team Slayer Rules",
               ChatColor.GRAY + "Two teams, and infinite respawns. 1 point per kill. First team to a certain point total wins."),
    Deathrun (ChatColor.GREEN + "Death Run Rules",
             ChatColor.GRAY + "One death, and lots of runners. Runners have to reach the end goal before the time limit or getting killed by death."),
    CaptureTheFlag (ChatColor.GREEN + "Capture the Flag Rules",
                    ChatColor.GRAY + "Capture the enemy flag(banner), by right clicking it, and escort it to your base beacon while protecting your own.",
                    ChatColor.GRAY + "To capture the enemy flag, right click on it, it will be placed on your head, then with it on your head right click your spawn, and you score 1 point.");

    private final String[] rules;

    Gamemodes(String... rules){
        this.rules = rules;
    }

    public static Gamemodes GetGamemode(String gamemode){
        switch(gamemode) {
            case "freeforall":
                return FreeForAll;
            case "infected":
                return Infected;
            case "oneinthequiver":
                return OneInTheQuiver;
            case "ringbearer":
                return Ringbearer;
            case "teamconquest":
                return TeamConquest;
            case "teamdeathmatch":
                return TeamDeathmatch;
            case "teamslayer":
                return TeamSlayer;
            case "deathrun":
                return Deathrun;
            case "capturetheflag":
                return CaptureTheFlag;
            default:
                return null;
        }
    }

    public static String[] GetRules(String gamemode) {
        if(GetGamemode(gamemode) == null)
            return new String[]{ChatColor.RED + "GAMEMODE RULES NOT IMPLEMENTED"};
        return GetGamemode(gamemode).getRules();
    }

    public String[] getRules() {
        return rules;
    }
}
