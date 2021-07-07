package com.mcmiddleearth.mcme.pvpplugin.util;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import org.bukkit.entity.Player;

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

    public static void addMember(Player player, Team... teams){
        addMember(player, Set.of(teams));
    }

    public static void addMember(Player player, Set<Team> teams){
        //TODO: make the general add member
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
}
