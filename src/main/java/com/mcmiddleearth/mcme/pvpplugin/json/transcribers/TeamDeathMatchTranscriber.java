package com.mcmiddleearth.mcme.pvpplugin.json.transcribers;

import com.mcmiddleearth.mcme.pvpplugin.exceptions.BadMaxPlayerException;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamDeathMatch;
import com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes.TeamDeathmatchRunner;

public class TeamDeathMatchTranscriber{
    public static void Transcribe(JSONMap jsonMap, TeamDeathmatchRunner runner) {
        JSONTeamDeathMatch gamemodeData = jsonMap.getJSONTeamDeathMatch();
        if (gamemodeData.getMaximumPlayers() == null) {
            throw new BadMaxPlayerException(jsonMap.getTitle() + " has null for max players for TeamDeathmatch");
        }
        runner.setMaxPlayers(gamemodeData.getMaximumPlayers());
        LocationTranscriber.setSpawnLocation(jsonMap.getTitle(), "TeamDeathMatch", "red", runner.getRed(), gamemodeData.getRedSpawn());
        LocationTranscriber.setSpawnLocation(jsonMap.getTitle(), "TeamDeathMatch", "blue", runner.getBlue(), gamemodeData.getBlueSpawn());
        LocationTranscriber.setSpawnLocation(jsonMap.getTitle(), "TeamDeathMatch", "spectators", runner.getSpectator(),jsonMap.getSpawn());
        AreaTranscriber.TranscribeArea(jsonMap.getTitle(), jsonMap.getRegionPoints(), runner);
    }
}
