package com.mcmiddleearth.mcme.pvpplugin.PVP;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Team {
    //TODO: Get functionality for dead members too and a forAll function
    @Getter
    @Setter
    private Set<Player> members = new HashSet<>();
    @Getter
    @Setter
    private Set<Player> deadMembers = new HashSet<>();
    @Getter
    private final String teamPrefix;
    @Getter
    private final ChatColor chatColor;

    public Team(String prefix, ChatColor color) {
        teamPrefix = prefix;
        chatColor = color;
    }

    public Boolean isInTeam(Player player) {
        return members.contains(player);
    }
    public Boolean allDead(){
        return members == deadMembers;
    }
    public void clear(){
        members.clear();
        deadMembers.clear();
    }
}
