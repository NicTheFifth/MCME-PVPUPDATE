package com.mcmiddleearth.mcme.pvpplugin.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.jsonData.JSONLocation;
import lombok.Getter;
import lombok.Setter;

public class DeathRun {
    @Getter@Setter
    JSONLocation deathSpawn;

    @Getter@Setter
    JSONLocation runnerSpawn;

    @Getter@Setter
    JSONLocation goal;

    @Getter@Setter
    Integer maximumPlayers;
}
