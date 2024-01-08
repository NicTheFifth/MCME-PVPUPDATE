package com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;

public abstract class JSONRedBlueSpawnGamemode extends JSONGamemode{
    private JSONLocation blueSpawn;
    private JSONLocation redSpawn;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public JSONLocation getBlueSpawn() {
        return this.blueSpawn;
    }
    public void setBlueSpawn(final JSONLocation blueSpawn) {
        this.blueSpawn = blueSpawn;
    }
    public JSONLocation getRedSpawn() {
        return this.redSpawn;
    }
    public void setRedSpawn(final JSONLocation redSpawn) {
        this.redSpawn = redSpawn;
    }
    //</editor-fold>
}
