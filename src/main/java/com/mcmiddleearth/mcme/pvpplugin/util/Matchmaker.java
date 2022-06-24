package com.mcmiddleearth.mcme.pvpplugin.util;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Set;

public class Matchmaker {
    PVPPlugin pvpPlugin;

    public Matchmaker(PVPPlugin pvpPlugin){
        this.pvpPlugin = pvpPlugin;
    }
    public void infectedMatchMake(Set<Player> players, Team infected, Team survivors){
        players.forEach(player -> {
            if(!(infected.getMembers().contains(player) || survivors.getMembers().contains(player))){
                addMember(player, infectedAdder(infected, survivors));
            }
        });
    }

    public Team infectedAdder(Team infected, Team survivors){
        if(5*getTotalELO(infected) > getTotalELO(survivors)){
            return survivors;
        }
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
        team.getMembers().add(player);
        player.getInventory().setContents(team.getKit().inventory.getContents());
    }

    public Double avgELO(Team team){
        return (double)(getTotalELO(team))/(double)(team.getMembers().size());
    }

    public Long getTotalELO(Team team){
        long retELO = 0L;
        for (Player player : team.getMembers()) {
            retELO += pvpPlugin.getPlayerstats().get(player.getUniqueId()).getElo();
        }
        return retELO;
    }

    public static void TeamSlayerMatchMake(Set<Player> players, Team red, Team blue) {
    }
}
