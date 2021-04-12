package com.mcmiddleearth.mcme.pvpplugin.Util.JSON.Gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.Util.JSON.JSONLocation;
import lombok.Getter;

public class JSONTeamDeathMatch extends JSONBaseGamemode{
    @Getter
    private JSONLocation blueSpawn;
    @Getter
    private JSONLocation redSpawn;
}