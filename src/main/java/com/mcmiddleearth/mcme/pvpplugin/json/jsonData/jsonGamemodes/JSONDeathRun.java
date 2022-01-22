package com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;

public class JSONDeathRun {
    JSONLocation deathSpawn;
    JSONLocation runnerSpawn;
    JSONLocation goal;
    Integer maximumPlayers;

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public JSONLocation getDeathSpawn() {
        return this.deathSpawn;
    }

    @SuppressWarnings("all")
    public void setDeathSpawn(final JSONLocation deathSpawn) {
        this.deathSpawn = deathSpawn;
    }

    @SuppressWarnings("all")
    public JSONLocation getRunnerSpawn() {
        return this.runnerSpawn;
    }

    @SuppressWarnings("all")
    public void setRunnerSpawn(final JSONLocation runnerSpawn) {
        this.runnerSpawn = runnerSpawn;
    }

    @SuppressWarnings("all")
    public JSONLocation getGoal() {
        return this.goal;
    }

    @SuppressWarnings("all")
    public void setGoal(final JSONLocation goal) {
        this.goal = goal;
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
