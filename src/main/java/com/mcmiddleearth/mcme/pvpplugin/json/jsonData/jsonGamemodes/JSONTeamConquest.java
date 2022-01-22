package com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;
import java.util.List;

public class JSONTeamConquest {
    JSONLocation blueSpawn;
    JSONLocation redSpawn;
    List<JSONLocation> capturePoints;
    Integer maximumPlayers;

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public JSONLocation getBlueSpawn() {
        return this.blueSpawn;
    }

    @SuppressWarnings("all")
    public void setBlueSpawn(final JSONLocation blueSpawn) {
        this.blueSpawn = blueSpawn;
    }

    @SuppressWarnings("all")
    public JSONLocation getRedSpawn() {
        return this.redSpawn;
    }

    @SuppressWarnings("all")
    public void setRedSpawn(final JSONLocation redSpawn) {
        this.redSpawn = redSpawn;
    }

    @SuppressWarnings("all")
    public List<JSONLocation> getCapturePoints() {
        return this.capturePoints;
    }

    @SuppressWarnings("all")
    public void setCapturePoints(final List<JSONLocation> capturePoints) {
        this.capturePoints = capturePoints;
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
