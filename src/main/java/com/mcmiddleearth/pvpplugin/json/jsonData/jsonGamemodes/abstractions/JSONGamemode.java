package com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions;

public abstract class JSONGamemode {
    private Integer maximumPlayers;
    public final Integer getMaximumPlayers() {
        return this.maximumPlayers;
    }
    public final void setMaximumPlayers(final Integer maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }
}
