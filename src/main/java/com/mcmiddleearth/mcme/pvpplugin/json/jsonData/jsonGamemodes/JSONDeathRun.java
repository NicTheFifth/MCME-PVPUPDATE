package com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;
import lombok.Getter;
import lombok.Setter;

public class JSONDeathRun {
    @Getter@Setter
    JSONLocation deathSpawn;

    @Getter@Setter
    JSONLocation runnerSpawn;

    @Getter@Setter
    JSONLocation goal;

    @Getter@Setter
    Integer maximumPlayers;
}
