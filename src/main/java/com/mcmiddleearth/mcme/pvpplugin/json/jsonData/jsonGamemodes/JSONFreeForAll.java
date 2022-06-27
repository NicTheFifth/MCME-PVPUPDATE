package com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;
import java.util.List;

public class JSONFreeForAll {
    List<JSONLocation> spawns;
    Integer maximumPlayers;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public List<JSONLocation> getSpawns() {
        return this.spawns;
    }
    public void setSpawns(final List<JSONLocation> spawns) {
        this.spawns = spawns;
    }

    public Integer getMaximumPlayers() {
        return this.maximumPlayers;
    }
    public void setMaximumPlayers(final Integer maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }
    //</editor-fold>
}
