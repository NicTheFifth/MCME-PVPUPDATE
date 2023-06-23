package com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;

public class JSONDeathRun extends JSONGamemode{
    JSONLocation deathSpawn;
    JSONLocation runnerSpawn;
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

    public JSONLocation getGoal() {
        return this.goal;
    }
    public void setGoal(final JSONLocation goal) {
        this.goal = goal;
    }
    //</editor-fold>
}
