package com.mcmiddleearth.mcme.pvpplugin.json.transcribers;

import com.mcmiddleearth.mcme.pvpplugin.exceptions.BadMaxPlayerException;
import com.mcmiddleearth.mcme.pvpplugin.exceptions.JSONLocationException;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes.JSONInfected;
import com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes.InfectedRunner;

import java.util.Collections;

public class InfectedTranscriber{
    public void Transcribe(JSONMap jsonMap, InfectedRunner runner) {
        JSONInfected infected = jsonMap.getJSONInfected();
        LocationTranscriber locationTranscriber = new LocationTranscriber();
        if(infected.getMaximumPlayers() == null)
            throw new BadMaxPlayerException(jsonMap.getTitle() + " has null for max players for infected");
        else {
            runner.setMaxPlayers(infected.getMaximumPlayers());
            try {
                runner.getInfected().setSpawnLocations(Collections.singletonList(locationTranscriber.TranscribeFromJSON(infected.getInfectedSpawn())));
            }
            catch(Exception e) {
                throw new JSONLocationException(jsonMap.getTitle(), "infected", "infected spawn");
            }
            try{
                runner.getSurvivors().setSpawnLocations(Collections.singletonList(locationTranscriber.TranscribeFromJSON(infected.getSurvivorSpawn())));
            }
            catch(Exception e){
                throw new JSONLocationException(jsonMap.getTitle(), "infected", "survivor spawn");
            }
        }
    }
}
