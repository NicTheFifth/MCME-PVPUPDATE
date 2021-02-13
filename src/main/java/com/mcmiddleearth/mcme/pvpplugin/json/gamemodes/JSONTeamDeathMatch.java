package com.mcmiddleearth.mcme.pvpplugin.json.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.json.JSONLocation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JSONTeamDeathMatch extends JSONBaseGamemode {
    private JSONLocation blueSpawn;
    private JSONLocation redSpawn;
}