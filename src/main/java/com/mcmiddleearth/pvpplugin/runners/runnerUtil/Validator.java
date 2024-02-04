package com.mcmiddleearth.pvpplugin.runners.runnerUtil;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamSlayer;

public class Validator {
    public static boolean isMapValid(JSONMap map){
        return map.getResourcePack() != null &&
            map.getSpawn() != null &&
            map.getRegionPoints() != null;
    }

    public static boolean canRunTeamSlayer(JSONMap map){
        JSONTeamSlayer teamSlayer = map.getJSONTeamSlayer();
        if(teamSlayer == null)
            return false;
        if(teamSlayer.getBlueSpawns() == null ||
            teamSlayer.getRedSpawns() == null ||
            teamSlayer.getMaximumPlayers() == null)
            return false;
        return teamSlayer.getRedSpawns().size() != 0 &&
            teamSlayer.getBlueSpawns().size() != 0;
    }
}
