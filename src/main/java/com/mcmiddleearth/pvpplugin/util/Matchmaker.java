package com.mcmiddleearth.pvpplugin.util;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Random;
import java.util.Set;

public class Matchmaker {
    public void addMember(Player player, Team... teams){
        addMember(player, Set.of(teams));
    }
    public void addMember(Player player, Set<Team> teams){
        Set<Pair<Integer, Team>> toAdd =
            new java.util.HashSet<>(Collections.emptySet());
        teams.forEach(team -> toAdd.add(Pair.of(team.getMembers().size(), team)));

        addMember(player, Collections.min(toAdd).getValue());
    }

    public static void addMember(Player player, Team team){
        team.getMembers().add(player);
        team.getKit().getInventory().accept(player);
    }
}
