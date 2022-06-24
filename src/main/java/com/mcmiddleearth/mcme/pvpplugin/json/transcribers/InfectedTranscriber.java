package com.mcmiddleearth.mcme.pvpplugin.json.transcribers;

import com.mcmiddleearth.mcme.pvpplugin.exceptions.BadMaxPlayerException;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes.JSONInfected;
import com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes.InfectedRunner;

public class InfectedTranscriber{
    public static void Transcribe(JSONMap jsonMap, InfectedRunner runner) {
        JSONInfected gamemodeData = jsonMap.getJSONInfected();
        if (gamemodeData.getMaximumPlayers() == null) {
            throw new BadMaxPlayerException(jsonMap.getTitle() + " has null for max players for infected");
        }
        runner.setMaxPlayers(gamemodeData.getMaximumPlayers());
        LocationTranscriber.setSpawnLocation(jsonMap.getTitle(), "Infected", "infected", runner.getInfected(), gamemodeData.getInfectedSpawn());
        LocationTranscriber.setSpawnLocation(jsonMap.getTitle(), "Infected", "survivor", runner.getSurvivors(), gamemodeData.getSurvivorSpawn());
        LocationTranscriber.setSpawnLocation(jsonMap.getTitle(), "Infected", "spectators", runner.getSpectator(),jsonMap.getSpawn());
        AreaTranscriber.TranscribeArea(jsonMap.getTitle(), jsonMap.getRegionPoints(), runner);
    }
}
