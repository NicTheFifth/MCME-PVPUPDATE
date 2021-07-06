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
        while(players.size() != infected.getMembers().size() + survivors.getMembers().size()){

        }
    }

    public static void addMember(Player player, Team... teams){
        addMember(player, Set.of(teams));
    }

    public static void addMember(Player player, Set<Team> teams){

    }
    public Long getTotalELO(Team team){
        long retELO = 0L;
        for (Player player : team.getMembers()) {
            retELO += pvpPlugin.getPlayerstats().get(player.getUniqueId()).getElo();
        }
        return retELO;
    }
}
