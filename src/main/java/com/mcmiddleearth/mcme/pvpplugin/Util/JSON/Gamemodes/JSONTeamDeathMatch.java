package com.mcmiddleearth.mcme.pvpplugin.Util.JSON.Gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.Util.JSON.JSONLocation;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class JSONTeamDeathMatch extends JSONBaseGamemode{
    @Getter
    private final Map<String, JSONLocation> spawns = new HashMap<>();
}