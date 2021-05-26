package com.mcmiddleearth.mcme.pvpplugin.json.transcribers;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes.JSONInfected;
import com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes.InfectedRunner;

import java.util.Collections;

public class InfectedTranscriber{
    public void Transcribe(JSONMap JSONMap, InfectedRunner runner) {
        JSONInfected infected = JSONMap.getJSONInfected();
        LocationTranscriber locationTranscriber = new LocationTranscriber();

        runner.setMaxPlayers(infected.getMaximumPlayers());
        runner.getInfected().setSpawnLocations(Collections.singletonList(locationTranscriber.TranscribeFromJSON(infected.getInfectedSpawn())));
        runner.getSurvivors().setSpawnLocations(Collections.singletonList(locationTranscriber.TranscribeFromJSON(infected.getSurvivorSpawn())));
    }
}
