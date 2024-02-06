package com.mcmiddleearth.pvpplugin.command;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamSlayer;

public class Validator {
    public static boolean isMapValid(JSONMap map){
        return map.getResourcePack() != null &&
            map.getSpawn() != null &&
            map.getRegionPoints() != null;
    }
    public static boolean canRunCaptureTheFlag(JSONMap map){
        return false;
    }
    public static boolean canRunDeathRun(JSONMap map){
        return false;
    }
    public static boolean canRunFreeForAll(JSONMap map){
        return false;
    }
    public static boolean canRunInfected(JSONMap map){
        return false;
    }
    public static boolean canRunOneInTheQuiver(JSONMap map){
        return false;
    }
    public static boolean canRunRingBearer(JSONMap map){
        return false;
    }
    public static boolean canRunTeamConquest(JSONMap map){
        return false;
    }
    public static boolean canRunTeamDeathMatch(JSONMap map){
        return false;
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
