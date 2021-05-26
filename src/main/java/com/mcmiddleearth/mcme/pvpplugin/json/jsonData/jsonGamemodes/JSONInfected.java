package com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;
import lombok.Getter;
import lombok.Setter;

public class JSONInfected {
    @Getter@Setter
    JSONLocation infectedSpawn;

    @Getter@Setter
    JSONLocation survivorSpawn;

    @Getter@Setter
    Integer maximumPlayers;
}
