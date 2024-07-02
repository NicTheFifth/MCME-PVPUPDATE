package com.mcmiddleearth.pvpplugin.command;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.*;

public class Validator {
    public static boolean isMapValid(JSONMap map){
        return map.getResourcePack() != null &&
            map.getSpawn() != null &&
            map.getRegionPoints() != null;
    }
    public static boolean canRunCaptureTheFlag(JSONMap map){
        JSONCaptureTheFlag captureTheFlag = map.getJSONCaptureTheFlag();
        if(captureTheFlag == null)
            return false;
        if(captureTheFlag.getBlueSpawns() == null ||
           captureTheFlag.getRedSpawns() == null ||
           captureTheFlag.getBlueFlag() == null ||
           captureTheFlag.getRedFlag() == null ||
           captureTheFlag.getMaximumPlayers() == null)
            return false;
        return !captureTheFlag.getBlueSpawns().isEmpty() &&
               !captureTheFlag.getRedSpawns().isEmpty();
    }
    public static boolean canRunDeathRun(JSONMap map){
        JSONDeathRun deathRun = map.getJSONDeathRun();
        if(deathRun == null)
            return false;
        return deathRun.getRunnerSpawn() != null &&
               deathRun.getDeathSpawn() != null &&
               deathRun.getGoal() != null &&
               deathRun.getKillHeight() != null &&
               deathRun.getMaximumPlayers() != null;
    }
    public static boolean canRunFreeForAll(JSONMap map){
        JSONFreeForAll freeForAll = map.getJSONFreeForAll();
        if(freeForAll == null)
            return false;
        if(freeForAll.getSpawns() == null ||
           freeForAll.getMaximumPlayers() == null)
            return false;
        return !freeForAll.getSpawns().isEmpty();
    }
    public static boolean canRunInfected(JSONMap map){
        JSONInfected infected = map.getJSONInfected();
        if(infected == null)
            return false;
        return infected.getSurvivorSpawn() != null &&
               infected.getInfectedSpawn() != null &&
               infected.getMaximumPlayers() != null;
    }
    public static boolean canRunOneInTheQuiver(JSONMap map){
        JSONOneInTheQuiver oneInTheQuiver = map.getJSONOneInTheQuiver();
        if(oneInTheQuiver == null)
            return false;
        if(oneInTheQuiver.getSpawns() == null ||
           oneInTheQuiver.getMaximumPlayers() == null)
            return false;
        return !oneInTheQuiver.getSpawns().isEmpty();
    }
    public static boolean canRunRingBearer(JSONMap map){
        JSONRingBearer ringBearer = map.getJSONRingBearer();
        if(ringBearer == null)
            return false;
        if(ringBearer.getBlueSpawns() == null ||
           ringBearer.getRedSpawns() == null ||
           ringBearer.getMaximumPlayers() == null)
            return false;
        return !ringBearer.getRedSpawns().isEmpty() &&
               !ringBearer.getBlueSpawns().isEmpty();
    }
    public static boolean canRunTeamConquest(JSONMap map){
        JSONTeamConquest teamConquest = map.getJSONTeamConquest();
        if(teamConquest == null)
            return false;
        if(teamConquest.getCapturePoints() == null ||
           teamConquest.getBlueSpawn() == null ||
           teamConquest.getRedSpawn() == null ||
           teamConquest.getMaximumPlayers() == null)
            return false;
        return !teamConquest.getCapturePoints().isEmpty();
    }
    public static boolean canRunTeamDeathMatch(JSONMap map){
        JSONTeamDeathMatch teamDeathMatch = map.getJSONTeamDeathMatch();
        if(teamDeathMatch == null)
            return false;
        return teamDeathMatch.getBlueSpawn() != null &&
               teamDeathMatch.getMaximumPlayers() != null &
               teamDeathMatch.getRedSpawn() != null;
    }
    public static boolean canRunTeamSlayer(JSONMap map){
        JSONTeamSlayer teamSlayer = map.getJSONTeamSlayer();
        if(teamSlayer == null)
            return false;
        if(teamSlayer.getBlueSpawns() == null ||
            teamSlayer.getRedSpawns() == null ||
            teamSlayer.getMaximumPlayers() == null)
            return false;
        return !teamSlayer.getRedSpawns().isEmpty() &&
                !teamSlayer.getBlueSpawns().isEmpty();
    }
}
