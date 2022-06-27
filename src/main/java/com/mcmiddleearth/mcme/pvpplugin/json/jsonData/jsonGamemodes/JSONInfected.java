package com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;

public class JSONInfected {
    JSONLocation infectedSpawn;
    JSONLocation survivorSpawn;
    Integer maximumPlayers;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    @SuppressWarnings("all")
    public JSONLocation getInfectedSpawn() {
        return this.infectedSpawn;
    }
    public void setInfectedSpawn(final JSONLocation infectedSpawn) {
        this.infectedSpawn = infectedSpawn;
    }

    public JSONLocation getSurvivorSpawn() {
        return this.survivorSpawn;
    }
    public void setSurvivorSpawn(final JSONLocation survivorSpawn) {
        this.survivorSpawn = survivorSpawn;
    }

    public Integer getMaximumPlayers() {
        return this.maximumPlayers;
    }
    public void setMaximumPlayers(final Integer maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }
    //</editor-fold>
}
