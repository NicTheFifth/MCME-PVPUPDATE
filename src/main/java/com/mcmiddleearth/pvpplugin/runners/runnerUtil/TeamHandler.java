package com.mcmiddleearth.pvpplugin.runners.runnerUtil;

import com.mcmiddleearth.pvpplugin.util.Team;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

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
            player.setGameMode(team.getGameMode());
        }
    }
    public static void spawn(Player player, Team... teams){
        Arrays.stream(teams).filter(team -> team.getMembers().contains(player))
            .findAny().ifPresent(team -> spawn(player, team));
    }
    public static void spawn(Player player, Team team) {
        player.teleport(team.getSpawnLocations().get((new Random()).nextInt(team.getSpawnLocations().size())));
        player.setGameMode(team.getGameMode());
    }

    public static void setGamemode(GameMode gamemode, Team...teams){
        setGamemode(gamemode, Set.of(teams));
    }

    public static void setGamemode(GameMode gamemode, Set<Team> teams){
        teams.forEach(team -> setGamemode(gamemode, team));
    }
    public static void setGamemode(GameMode gamemode, Team team){
        team.getMembers().forEach(player -> player.setGameMode(gamemode));
    }

    @SafeVarargs
    public static <T> void addToTeam(Function<Team, Comparable<? super T>> comparer,
                                     Pair<Team, Runnable>... teamAdderPairs){
        Stream.of(teamAdderPairs)
            .map(teamAdderPair ->
                Pair.of(comparer.apply(teamAdderPair.getKey()),
                    teamAdderPair.getValue()))
            .min(Pair::compareTo)
            .ifPresent(teamAdderPair ->
                teamAdderPair.getValue().run());
    }
}
