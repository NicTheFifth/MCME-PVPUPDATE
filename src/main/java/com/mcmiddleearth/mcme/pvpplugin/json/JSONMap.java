package com.mcmiddleearth.mcme.pvpplugin.json;

import com.mcmiddleearth.mcme.pvpplugin.json.gamemodes.JSONInfected;
import com.mcmiddleearth.mcme.pvpplugin.json.gamemodes.JSONTeamDeathMatch;
import lombok.Getter;

import java.util.ArrayList;

public class JSONMap{
        @Getter
        private String title;
        @Getter
        private JSONLocation spectatorSpawn;
        @Getter
        private String resourcePack;
        @Getter
        private ArrayList<JSONLocation> regionPoints;

        // Gamemodes
        @Getter
        private JSONTeamDeathMatch teamDeathMatch;
        @Getter
        private JSONInfected infected;
}

