package com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONRedBlueSpawnGamemode;

import java.util.List;

public class JSONTeamConquest extends JSONRedBlueSpawnGamemode {
    List<JSONLocation> capturePoints;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public List<JSONLocation> getCapturePoints() {
        return this.capturePoints;
    }
    public void setCapturePoints(final List<JSONLocation> capturePoints) {
        this.capturePoints = capturePoints;
    }
    //</editor-fold>
}
