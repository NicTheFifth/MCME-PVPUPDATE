package com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class JSONTeamConquest {
    @Getter@Setter
    JSONLocation blueSpawn;

    @Getter@Setter
    JSONLocation redSpawn;

    @Getter@Setter
    List<JSONLocation> capturePoints;

    @Getter@Setter
    Integer maximumPlayers;
}
