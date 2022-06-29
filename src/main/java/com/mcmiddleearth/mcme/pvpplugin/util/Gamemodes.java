package com.mcmiddleearth.mcme.pvpplugin.util;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes.JSONGamemode;
import com.mcmiddleearth.mcme.pvpplugin.runners.GamemodeRunner;
import com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes.InfectedRunner;
import com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes.TeamDeathmatchRunner;
import com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes.TeamSlayerRunner;
import org.bukkit.ChatColor;

import java.util.function.Function;

public enum Gamemodes {
    CaptureTheFlag ((JSONMap::getJSONCaptureTheFlag),
            (x -> new InfectedRunner(x,false)),
            (x -> new InfectedRunner(x,true)),
            ChatColor.GREEN + "Capture the Flag Rules",
            ChatColor.GRAY + "Capture the enemy flag(banner), by right clicking it, and escort it to your base beacon while protecting your own.",
            ChatColor.GRAY + "To capture the enemy flag, right click on it, it will be placed on your head, then with it on your head right click your spawn, and you score 1 point."),
    Deathrun ((JSONMap::getJSONDeathRun),
            (x -> new InfectedRunner(x,false)),
            (x -> new InfectedRunner(x,true)),
            ChatColor.GREEN + "Death Run Rules",
            ChatColor.GRAY + "One death, and lots of runners. Runners have to reach the end goal before the time limit or getting killed by death."),
    FreeForAll ((JSONMap::getJSONFreeForAll),
            (x -> new InfectedRunner(x,false)),
            (x -> new InfectedRunner(x,true)),
            ChatColor.GREEN + "Free For All Rules",
            ChatColor.GRAY + "Every man for himself, madly killing everyone! Highest number of kills wins."),
    Infected ((JSONMap::getJSONInfected),
            (x -> new InfectedRunner(x,false)),
            (x -> new InfectedRunner(x,true)),
            ChatColor.GREEN + "Infected Rules",
            ChatColor.GRAY + "Everyone starts as a Survivor, except one person, who is Infected. Infected gets a Speed effect, but has less armor.",
            ChatColor.GRAY + "If a Survivor is killed, they become Infected. Infected players have infinite respawns.",
            ChatColor.GRAY + "If all Survivors are infected, Infected team wins. If the time runs out with Survivors remaining, Survivors win."),
    OneInTheQuiver ((JSONMap::getJSONOneInTheQuiver),
            (x -> new InfectedRunner(x,false)),
            (x -> new InfectedRunner(x,true)),
            ChatColor.GREEN + "One in the Quiver Rules",
                  ChatColor.GRAY + "Everyone gets an axe, a bow, and one arrow, which kills in 1 shot if the bow is fully drawn.",
                  ChatColor.GRAY + "Every man is fighting for himself. If they get a kill or die, they get another arrow, up to a max of 5 arrows",
                  ChatColor.GRAY + "First to 21 kills wins."),
    Ringbearer ((JSONMap::getJSONRingBearer),
            (x -> new InfectedRunner(x,false)),
            (x -> new InfectedRunner(x,true)),
            ChatColor.GREEN + "Ringbearer Rules",
                ChatColor.GRAY + "Two teams, each with a ringbearer, who gets The One Ring (which of course gives invisibility)",
                ChatColor.GRAY + "As long as the ringbearer is alive, the team can respawn.",
                ChatColor.GRAY + "Once the ringbearer dies, that team cannot respawn. The first team to run out of members loses."),
    TeamConquest ((JSONMap::getJSONTeamConquest),
            (x -> new InfectedRunner(x,false)),
            (x -> new InfectedRunner(x,true)),
            ChatColor.GREEN + "Team Conquest Rules",
                  ChatColor.GRAY + "Two teams. There are 3 beacons, which each team can capture by repeatedly right clicking the beacon.",
                  ChatColor.GRAY + "Points are awarded on kills, based on the difference between each team's number of beacons.",
                  ChatColor.GRAY + "i.e. if Red has 3 beacons and Blue has 0, Red gets 3 point per kill. If Red has 1 and Blue has 2, Red doesn't get points for a kill.",
                  ChatColor.GRAY + "First team to a certain point total wins."),
    TeamDeathmatch ((JSONMap::getJSONTeamDeathMatch),
            (x -> new TeamDeathmatchRunner(x,false)),
            (x -> new TeamDeathmatchRunner(x,true)),
            ChatColor.GREEN + "Team Deathmatch Rules",
                   ChatColor.GRAY + "Two teams, and no respawns. First team to run out of players loses."),
    TeamSlayer ((JSONMap::getJSONTeamSlayer),
            (x -> new TeamSlayerRunner(x,false)),
            (x -> new TeamSlayerRunner(x,true)),
            ChatColor.GREEN + "Team Slayer Rules",
               ChatColor.GRAY + "Two teams, and infinite respawns. 1 point per kill. First team to a certain point total wins.");

    private final String[] rules;

    private final Function<JSONMap, JSONGamemode> getJSONGamemode;

    private final Function<JSONMap,GamemodeRunner> publicRunner;

    private final Function<JSONMap, GamemodeRunner> privateRunner;

    Gamemodes(Function<JSONMap, JSONGamemode> getJSONGamemode,
              Function<JSONMap,GamemodeRunner> publicRunner,
              Function<JSONMap, GamemodeRunner> privateRunner,
              String... rules){
        this.getJSONGamemode = getJSONGamemode;
        this.publicRunner = publicRunner;
        this.privateRunner = privateRunner;
        this.rules = rules;
    }

    //<editor-fold desc="Getters and Setters">
    public static Gamemodes getGamemode(String gamemode){
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

    public static String[] getRules(String gamemode) {
        if(getGamemode(gamemode) == null)
            return new String[]{ChatColor.RED + "GAMEMODE RULES NOT IMPLEMENTED"};
        return getGamemode(gamemode).getRulesPrivate();
    }

    private String[] getRulesPrivate() {
        return rules;
    }

    public Function<JSONMap, JSONGamemode> getJSONGamemode() {
        return getJSONGamemode;
    }

    public Function<JSONMap, GamemodeRunner> getPublicRunner() {
        return publicRunner;
    }

    public Function<JSONMap, GamemodeRunner> getPrivateRunner(){
        return privateRunner;
    }
    //</editor-fold>
}
