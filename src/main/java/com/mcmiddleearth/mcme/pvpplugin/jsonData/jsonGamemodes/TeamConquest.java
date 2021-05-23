package com.mcmiddleearth.mcme.pvpplugin.jsonData.jsonGamemodes;

import com.mcmiddleearth.mcme.pvpplugin.jsonData.JSONLocation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class TeamConquest {
    @Getter@Setter
    JSONLocation blueSpawn;

    @Getter@Setter
    JSONLocation redSpawn;

    @Getter@Setter
    List<JSONLocation> capturePoints;

    @Getter@Setter
    Integer maximumPlayers;
}
