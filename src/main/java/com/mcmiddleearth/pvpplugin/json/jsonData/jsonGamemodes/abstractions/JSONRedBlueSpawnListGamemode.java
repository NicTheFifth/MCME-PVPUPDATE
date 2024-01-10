package com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;

import java.util.List;

public abstract class JSONRedBlueSpawnListGamemode extends JSONGamemode{
    private List<JSONLocation> blueSpawns;
    private List<JSONLocation> redSpawns;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public List<JSONLocation> getBlueSpawns() {
        return this.blueSpawns;
    }
    public void setBlueSpawns(final List<JSONLocation> blueSpawns) {
        this.blueSpawns = blueSpawns;
    }

    public List<JSONLocation> getRedSpawns() {
        return this.redSpawns;
    }
    public void setRedSpawns(final List<JSONLocation> redSpawns) {
        this.redSpawns = redSpawns;
    }
    //</editor-fold>
}
