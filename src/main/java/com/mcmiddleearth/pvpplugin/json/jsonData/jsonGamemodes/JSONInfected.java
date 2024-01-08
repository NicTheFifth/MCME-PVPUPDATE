package com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONGamemode;

public class JSONInfected extends JSONGamemode {
    JSONLocation infectedSpawn;
    JSONLocation survivorSpawn;

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
    //</editor-fold>
}
