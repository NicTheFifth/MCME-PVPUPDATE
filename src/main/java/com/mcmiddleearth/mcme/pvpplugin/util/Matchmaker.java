package com.mcmiddleearth.mcme.pvpplugin.util;

import org.bukkit.entity.Player;

import java.util.Set;

public class Matchmaker {
    public Matchmaker(){}
    public void addMember(Player player, Team... teams){
        addMember(player, Set.of(teams));
    }

    public void addMember(Player player, Set<Team> teams){

    }
}
