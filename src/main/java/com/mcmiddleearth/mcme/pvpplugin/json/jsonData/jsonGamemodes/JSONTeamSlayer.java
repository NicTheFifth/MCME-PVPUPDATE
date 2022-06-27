package com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;

public class JSONTeamSlayer {
    JSONLocation blueSpawn;
    JSONLocation redSpawn;
    Integer maximumPlayers;

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

    public Integer getMaximumPlayers() {
        return this.maximumPlayers;
    }
    public void setMaximumPlayers(final Integer maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }
    //</editor-fold>
}
