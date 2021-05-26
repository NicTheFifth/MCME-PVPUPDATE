package com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;
import lombok.Getter;
import lombok.Setter;

public class JSONTeamDeathMatch {
    @Getter@Setter
    JSONLocation blueSpawn;

    @Getter@Setter
    JSONLocation redSpawn;

    @Getter@Setter
    Integer maximumPlayers;
}
