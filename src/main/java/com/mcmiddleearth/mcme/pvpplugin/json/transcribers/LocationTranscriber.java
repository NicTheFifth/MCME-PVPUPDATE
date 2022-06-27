package com.mcmiddleearth.mcme.pvpplugin.json.transcribers;

import com.mcmiddleearth.mcme.pvpplugin.exceptions.JSONLocationException;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.mcme.pvpplugin.util.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Collections;

public class LocationTranscriber {
    static void setSpawnLocation(String title, String gamemode, String teamType, Team team, JSONLocation spawn) {
        try {
            team.setSpawnLocations(Collections.singletonList(LocationTranscriber.TranscribeFromJSON(spawn)));
        } catch (Exception e) {
            throw new JSONLocationException(title, gamemode, teamType + " spawn");
        }
    }

    public static Location TranscribeFromJSON(JSONLocation loc){
        return new Location(Bukkit.getWorld(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ());
    }
}
