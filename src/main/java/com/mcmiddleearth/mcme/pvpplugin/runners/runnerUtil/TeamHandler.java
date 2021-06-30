package com.mcmiddleearth.mcme.pvpplugin.runners.runnerUtil;

import com.mcmiddleearth.mcme.pvpplugin.util.Team;
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
        for (Player player: team.getMembers()) {
            player.teleport(team.getSpawnLocations().get(spawnNum%team.getSpawnLocations().size()));
            spawnNum++;
        }
    }
}
