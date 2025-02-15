package com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;

import java.util.List;

public abstract class JSONSpawnListGamemode extends JSONGamemode{
    private List<JSONLocation> spawns;

    //<editor-fold desc="Getters and Setters">
    public List<JSONLocation> getSpawns() {
        return spawns;
    }

    public void setSpawns(List<JSONLocation> spawns) {
        this.spawns = spawns;
    }
    //</editor-fold>

}
