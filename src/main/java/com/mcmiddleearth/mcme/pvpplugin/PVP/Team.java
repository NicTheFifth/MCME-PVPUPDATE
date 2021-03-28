package com.mcmiddleearth.mcme.pvpplugin.PVP;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Team {

    private Set<Player> members = new HashSet<>();
    private Set<Player> deadMembers = new HashSet<>();
    private final String teamPrefix;
    private final ChatColor chatColor;

    private Set<Location> spawnLocations = new HashSet<>();

    public Team(String prefix, ChatColor color) {
        teamPrefix = prefix;
        chatColor = color;
    }

    public boolean isInTeam(Player player) {
        return members.contains(player);
    }

    public boolean allDead(){
        return deadMembers.containsAll(members);
    }

    public void setSpawnLocations(Set<Location> locations) {
        this.spawnLocations = locations;
    }

    public void addMemberAndSpawn(Player player) {
        addMember(player);
        spawnMember(player);
    }
    public void spawnTeam() {
        members.forEach(this::spawnMember);
    }

    private void addMember(Player player) {
        members.add(player);
    }

    private void spawnMember(Player player) {
        if (isInTeam(player)) {
            player.teleport(getRandomSpawnLocation());
        }
    }

    private Location getRandomSpawnLocation() {
        return spawnLocations.stream().skip(new Random().nextInt(spawnLocations.size())).findFirst().orElse(null);
    }

}
