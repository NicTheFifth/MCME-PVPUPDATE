package com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes;

public abstract class JSONGamemode {
    Integer maximumPlayers;
    public Integer getMaximumPlayers() {
        return this.maximumPlayers;
    }
    public void setMaximumPlayers(final Integer maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }
}
