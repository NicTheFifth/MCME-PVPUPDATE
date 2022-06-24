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

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Set<Player> getMembers() {
        return this.members;
    }
    public void setMembers(final Set<Player> members) {
        this.members = members;
    }

    public Set<Player> getDeadMembers() {
        return this.deadMembers;
    }
    public void setDeadMembers(final Set<Player> deadMembers) {
        this.deadMembers = deadMembers;
    }

    public Color getTeamColour() {
        return this.teamColour;
    }
    public void setTeamColour(final Color teamColour) {
        this.teamColour = teamColour;
    }

    public String getPrefix() {
        return this.Prefix;
    }
    public void setPrefix(final String Prefix) {
        this.Prefix = Prefix;
    }

    public Kit getKit() {
        return this.kit;
    }
    public void setKit(final Kit kit) {
        this.kit = kit;
    }

    public List<Location> getSpawnLocations() {
        return this.spawnLocations;
    }
    public void setSpawnLocations(final List<Location> spawnLocations) {
        this.spawnLocations = spawnLocations;
    }
    //</editor-fold>
}
