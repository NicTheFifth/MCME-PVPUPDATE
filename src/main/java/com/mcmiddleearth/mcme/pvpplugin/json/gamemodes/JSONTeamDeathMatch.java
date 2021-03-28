package com.mcmiddleearth.mcme.pvpplugin.json.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.json.JSONLocation;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class JSONTeamDeathMatch extends JSONBaseGamemode {
    private Set<JSONLocation> blueSpawn;
    private Set<JSONLocation> redSpawn;
}