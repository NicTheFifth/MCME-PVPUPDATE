package com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;

public class JSONDeathRun {
    JSONLocation deathSpawn;
    JSONLocation runnerSpawn;
    JSONLocation goal;
    Integer maximumPlayers;

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

    public Integer getMaximumPlayers() {
        return this.maximumPlayers;
    }
    public void setMaximumPlayers(final Integer maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }
    //</editor-fold>
}
