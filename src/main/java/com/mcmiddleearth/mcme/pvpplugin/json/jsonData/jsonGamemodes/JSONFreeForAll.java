package com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;
import java.util.List;

public class JSONFreeForAll {
    List<JSONLocation> spawns;
    Integer maximumPlayers;

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public List<JSONLocation> getSpawns() {
        return this.spawns;
    }

    @SuppressWarnings("all")
    public void setSpawns(final List<JSONLocation> spawns) {
        this.spawns = spawns;
    }

    @SuppressWarnings("all")
    public Integer getMaximumPlayers() {
        return this.maximumPlayers;
    }

    @SuppressWarnings("all")
    public void setMaximumPlayers(final Integer maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }
    //</editor-fold>
}
