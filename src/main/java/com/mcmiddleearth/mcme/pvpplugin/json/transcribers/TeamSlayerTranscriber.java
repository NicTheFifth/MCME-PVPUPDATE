package com.mcmiddleearth.mcme.pvpplugin.json.transcribers;

import com.mcmiddleearth.mcme.pvpplugin.exceptions.BadMaxPlayerException;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamSlayer;
import com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes.TeamSlayerRunner;

public class TeamSlayerTranscriber{
    public static void Transcribe(JSONMap jsonMap, TeamSlayerRunner runner) {
        JSONTeamSlayer gamemodeData = jsonMap.getJSONTeamSlayer();
        if (gamemodeData.getMaximumPlayers() == null) {
            throw new BadMaxPlayerException(jsonMap.getTitle() + " has null for max players for infected");
        }
        runner.setMaxPlayers(gamemodeData.getMaximumPlayers());
        LocationTranscriber.setSpawnLocation(jsonMap.getTitle(), "TeamSlayer", "red", runner.getRed(), gamemodeData.getRedSpawn());
        LocationTranscriber.setSpawnLocation(jsonMap.getTitle(), "TeamSlayer", "blue", runner.getBlue(), gamemodeData.getBlueSpawn());
        LocationTranscriber.setSpawnLocation(jsonMap.getTitle(), "TeamSlayer", "spectators", runner.getSpectator(),jsonMap.getSpawn());
        AreaTranscriber.TranscribeArea(jsonMap.getTitle(), jsonMap.getRegionPoints(), runner);
    }

}
