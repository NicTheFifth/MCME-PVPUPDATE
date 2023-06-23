package com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import java.util.List;

public class JSONFreeForAll extends JSONGamemode{
    List<JSONLocation> spawns;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public List<JSONLocation> getSpawns() {
        return this.spawns;
    }
    public void setSpawns(final List<JSONLocation> spawns) {
        this.spawns = spawns;
    }
    //</editor-fold>
}
