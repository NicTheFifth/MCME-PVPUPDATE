package com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;

public class JSONInfected {
    JSONLocation infectedSpawn;
    JSONLocation survivorSpawn;
    Integer maximumPlayers;

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public JSONLocation getInfectedSpawn() {
        return this.infectedSpawn;
    }

    @SuppressWarnings("all")
    public void setInfectedSpawn(final JSONLocation infectedSpawn) {
        this.infectedSpawn = infectedSpawn;
    }

    @SuppressWarnings("all")
    public JSONLocation getSurvivorSpawn() {
        return this.survivorSpawn;
    }

    @SuppressWarnings("all")
    public void setSurvivorSpawn(final JSONLocation survivorSpawn) {
        this.survivorSpawn = survivorSpawn;
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
