package com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;
import java.util.List;

public class JSONTeamConquest extends JSONGamemode{
    JSONLocation blueSpawn;
    JSONLocation redSpawn;
    List<JSONLocation> capturePoints;

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

    public List<JSONLocation> getCapturePoints() {
        return this.capturePoints;
    }
    public void setCapturePoints(final List<JSONLocation> capturePoints) {
        this.capturePoints = capturePoints;
    }
    //</editor-fold>
}
