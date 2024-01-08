package com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONRedBlueSpawnGamemode;

public class JSONCaptureTheFlag extends JSONRedBlueSpawnGamemode {
    JSONLocation blueFlag;
    JSONLocation redFlag;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public JSONLocation getBlueFlag() {
        return this.blueFlag;
    }
    public void setBlueFlag(final JSONLocation blueFlag) {
        this.blueFlag = blueFlag;
    }
    public JSONLocation getRedFlag() {
        return this.redFlag;
    }
    public void setRedFlag(final JSONLocation redFlag) {
        this.redFlag = redFlag;
    }
    //</editor-fold>
}
