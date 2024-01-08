package com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONGamemode;

import java.util.List;

public class JSONTeamSlayer extends JSONGamemode {
    List<JSONLocation> blueSpawn;
    List<JSONLocation> redSpawn;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public List<JSONLocation> getBlueSpawn() {
        return this.blueSpawn;
    }
    public void setBlueSpawn(final List<JSONLocation> blueSpawn) {
        this.blueSpawn = blueSpawn;
    }

    public List<JSONLocation> getRedSpawn() {
        return this.redSpawn;
    }
    public void setRedSpawn(final List<JSONLocation> redSpawn) {
        this.redSpawn = redSpawn;
    }
    //</editor-fold>
}
