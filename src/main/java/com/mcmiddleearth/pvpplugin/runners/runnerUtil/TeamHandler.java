package com.mcmiddleearth.pvpplugin.runners.runnerUtil;

import com.mcmiddleearth.pvpplugin.util.Team;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Set;

public class TeamHandler {

    public static void spawnAll(Team...teams){
        spawnAll(Set.of(teams));
    }

    public static void spawnAll(Set<Team> teams){
        teams.forEach(TeamHandler::spawnAll);
    }

    public static void spawnAll(Team team){
        int spawnNum = 0;
        for (Player player: team.getAliveMembers()) {
            player.teleport(team.getSpawnLocations().get(spawnNum%team.getSpawnLocations().size()));
            spawnNum++;
        }
    }
    public static void spawn(Player player, Team team) {
        player.teleport(team.getSpawnLocations().get(0));
    }

    public static void setGamemode(GameMode gamemode, Team...teams){
        setGamemode(gamemode, Set.of(teams));
    }

    public static void setGamemode(GameMode gamemode, Set<Team> teams){
        teams.forEach(team -> setGamemode(gamemode, team));
    }
    public static void setGamemode(GameMode gamemode, Team team){
        team.getActiveMembers().forEach(player -> player.setGameMode(gamemode));
    }
}
