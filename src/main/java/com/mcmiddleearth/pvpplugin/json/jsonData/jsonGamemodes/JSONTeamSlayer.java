package com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;

public class JSONTeamSlayer extends JSONGamemode{
    JSONLocation blueSpawn;
    JSONLocation redSpawn;

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
