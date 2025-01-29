package com.mcmiddleearth.pvpplugin.util;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Team {
    protected Set<Player> members = new HashSet<>();
    protected Set<Player> onlineMembers = new HashSet<>();
    Color teamColour;
    NamedTextColor chatColor;
    String Prefix;
    Kit kit;
    List<Location> spawnLocations = new ArrayList<>();
    GameMode gameMode;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Set<Player> getMembers() {
        return this.members;
    }
    public Set<Player> getOnlineMembers() {
        return onlineMembers;
    }
    public Color getTeamColour() {
        return this.teamColour;
    }
    public void setTeamColour(final Color teamColour) {
        this.teamColour = teamColour;
    }
    public NamedTextColor getChatColor(){return this.chatColor;}
    public void setChatColor(NamedTextColor chatColor){this.chatColor = chatColor;}
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
    public void setSpawnLocations(List<Location> spawnLocations) {
        this.spawnLocations = spawnLocations;
    }
    public GameMode getGameMode(){
        return this.gameMode;
    }
    public void setGameMode(GameMode gameMode){
        this.gameMode = gameMode;
    }
    //</editor-fold>
}
