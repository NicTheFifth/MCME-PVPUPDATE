package com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONGamemode;

public class JSONDeathRun extends JSONGamemode {
    JSONLocation deathSpawn;
    JSONLocation runnerSpawn;
    Integer killHeight;
    JSONLocation goal;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public JSONLocation getDeathSpawn() {
        return this.deathSpawn;
    }
    public void setDeathSpawn(final JSONLocation deathSpawn) {
        this.deathSpawn = deathSpawn;
    }

    public JSONLocation getRunnerSpawn() {
        return this.runnerSpawn;
    }
    public void setRunnerSpawn(final JSONLocation runnerSpawn) {
        this.runnerSpawn = runnerSpawn;
    }

    public Integer getKillHeight() { return this.killHeight;}
    public void setKillHeight(Integer killHeight){this.killHeight = killHeight;}

    public JSONLocation getGoal() {
        return this.goal;
    }
    public void setGoal(final JSONLocation goal) {
        this.goal = goal;
    }
    //</editor-fold>
}
