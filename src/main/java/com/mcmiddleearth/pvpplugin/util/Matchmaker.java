package com.mcmiddleearth.pvpplugin.util;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Random;
import java.util.Set;

public class Matchmaker {
    public void infectedMatchMake(Set<Player> players, Team infected, Team survivors){
        if(infected.getActiveMembers().isEmpty())
            addMember(players.stream().skip((new Random().nextInt(players.size()))).findFirst().get(), infected);
        players.forEach(player -> {
            if(!(infected.getActiveMembers().contains(player) || survivors.getActiveMembers().contains(player))){
                addMember(player, infectedAdder(infected, survivors));
            }
        });
    }

    public Team infectedAdder(Team infected, Team survivors){
        if(infected.getActiveMembers().isEmpty())
            return infected;
        if(5*getTotalELO(infected) >= getTotalELO(survivors))
            return survivors;
        return infected;
    }

    public static void teamDeathmatchMatchMake(Set<Player> players, Team red, Team blue){

    }

    public void addMember(Player player, Team... teams){
        addMember(player, Set.of(teams));
    }

    public void addMember(Player player, Set<Team> teams){
        Set<Pair<Long, Team>> toAdd = new java.util.HashSet<>(Collections.emptySet());
        teams.forEach(team -> toAdd.add(Pair.of(getTotalELO(team), team)));

        addMember(player, Collections.min(toAdd).getValue());
    }

    public static void addMember(Player player, Team team){
        team.getActiveMembers().add(player);
        team.getAliveMembers().add(player);
        team.getKit().getInventory().apply(player);
    }

    public Double avgELO(Team team){
        return (double)(getTotalELO(team))/(double)(team.getActiveMembers().size());
    }

    public Long getTotalELO(Team team){
        long retELO = 0L;
        for (Player player : team.getActiveMembers()) {
            retELO += PVPPlugin.getInstance().getPlayerstats().get(player.getUniqueId()).getElo();
        }
        return retELO;
    }

    public static void TeamSlayerMatchMake(Set<Player> players, Team red, Team blue) {
    }
}
