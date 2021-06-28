package com.mcmiddleearth.mcme.pvpplugin.util;

import org.bukkit.entity.Player;

import java.util.Set;

public class Matchmaker {
    public static void addMember(Player player, Team... teams){
        addMember(player, Set.of(teams));
    }

    public static void addMember(Player player, Set<Team> teams){

    }
}
