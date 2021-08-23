package com.mcmiddleearth.mcme.pvpplugin.json.transcribers;

import com.mcmiddleearth.mcme.pvpplugin.exceptions.BadMaxPlayerException;
import com.mcmiddleearth.mcme.pvpplugin.exceptions.JSONLocationException;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes.JSONInfected;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamDeathMatch;
import com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes.InfectedRunner;
import com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes.TeamDeathmatchRunner;
import com.mcmiddleearth.mcme.pvpplugin.util.Team;

import java.util.Collections;

public class TeamDeathMatchTranscriber {
    public static void Transcribe(JSONMap jsonMap, TeamDeathmatchRunner runner) {
        JSONTeamDeathMatch gamemodeData = jsonMap.getJSONTeamDeathMatch();
        if (gamemodeData.getMaximumPlayers() == null) {
            throw new BadMaxPlayerException(jsonMap.getTitle() + " has null for max players for TeamDeathmatch");
        }
        runner.setMaxPlayers(gamemodeData.getMaximumPlayers());
        setSpawnLocation(jsonMap.getTitle(), "infected", runner.getRed(), gamemodeData.getRedSpawn());
        setSpawnLocation(jsonMap.getTitle(), "survivor", runner.getBlue(), gamemodeData.getBlueSpawn());
        setSpawnLocation(jsonMap.getTitle(), "spectators", runner.getSpectator(),jsonMap.getSpawn());
        AreaTranscriber.TranscribeArea(jsonMap.getTitle(), jsonMap.getRegionPoints(), runner);
    }

    private static void setSpawnLocation(String title, String teamType, Team team, JSONLocation spawn) {
        try {
            team.setSpawnLocations(Collections.singletonList(LocationTranscriber.TranscribeFromJSON(spawn)));
        } catch (Exception e) {
            throw new JSONLocationException(title, "infected", teamType + " spawn");
        }
    }
}
