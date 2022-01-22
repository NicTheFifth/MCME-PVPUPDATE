package com.mcmiddleearth.mcme.pvpplugin.util;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Team {
    Set<Player> members = Collections.emptySet();
    Set<Player> deadMembers = Collections.emptySet();
    Color teamColour;
    String Prefix;
    Kit kit;
    List<Location> spawnLocations;

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public Set<Player> getMembers() {
        return this.members;
    }

    @SuppressWarnings("all")
    public void setMembers(final Set<Player> members) {
        this.members = members;
    }

    @SuppressWarnings("all")
    public Set<Player> getDeadMembers() {
        return this.deadMembers;
    }

    @SuppressWarnings("all")
    public void setDeadMembers(final Set<Player> deadMembers) {
        this.deadMembers = deadMembers;
    }

    @SuppressWarnings("all")
    public Color getTeamColour() {
        return this.teamColour;
    }

    @SuppressWarnings("all")
    public void setTeamColour(final Color teamColour) {
        this.teamColour = teamColour;
    }

    @SuppressWarnings("all")
    public String getPrefix() {
        return this.Prefix;
    }

    @SuppressWarnings("all")
    public void setPrefix(final String Prefix) {
        this.Prefix = Prefix;
    }

    @SuppressWarnings("all")
    public Kit getKit() {
        return this.kit;
    }

    @SuppressWarnings("all")
    public void setKit(final Kit kit) {
        this.kit = kit;
    }

    @SuppressWarnings("all")
    public List<Location> getSpawnLocations() {
        return this.spawnLocations;
    }

    @SuppressWarnings("all")
    public void setSpawnLocations(final List<Location> spawnLocations) {
        this.spawnLocations = spawnLocations;
    }
    //</editor-fold>
}
