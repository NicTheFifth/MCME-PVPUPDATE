package com.mcmiddleearth.mcme.pvpplugin.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.jsonData.JSONLocation;
import lombok.Getter;
import lombok.Setter;

public class Infected {
    @Getter@Setter
    JSONLocation infectedSpawn;

    @Getter@Setter
    JSONLocation survivorSpawn;

    @Getter@Setter
    Integer maximumPlayers;
}
