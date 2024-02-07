package com.mcmiddleearth.pvpplugin.runners.runnerUtil;

import com.mcmiddleearth.pvpplugin.util.Team;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;
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
        player.teleport(team.getSpawnLocations().get(randomRespawnIndex(team)));
        player.setGameMode(team.getGameMode());
    }
    @SafeVarargs
    public static <T> void addToTeam(Function<Team, Comparable<? super T>> comparer,
                                     Pair<Team, Runnable>... teamAdderPairs){
        Stream.of(teamAdderPairs)
            .map(teamAdderPair ->
                Pair.of(comparer.apply(teamAdderPair.getKey()),
                    teamAdderPair.getValue()))
            .min((fst, snd) -> new CompareToBuilder().append(fst.getLeft(),
                snd.getLeft()).toComparison())
            .ifPresent(teamAdderPair ->
                teamAdderPair.getValue().run());
    }

    public static void respawn(PlayerRespawnEvent e, Team team) {
        e.setRespawnLocation(
            team.getSpawnLocations().get(randomRespawnIndex(team))
        );
    }

    private static int randomRespawnIndex(Team team){
        return (new Random())
            .nextInt(team.getSpawnLocations().size());
    }
}
