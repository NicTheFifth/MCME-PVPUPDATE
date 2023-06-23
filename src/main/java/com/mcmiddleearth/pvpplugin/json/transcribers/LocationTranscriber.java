package com.mcmiddleearth.pvpplugin.json.transcribers;

import com.mcmiddleearth.pvpplugin.exceptions.JSONLocationException;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.util.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

public class LocationTranscriber {
    static void setSpawnLocation(String title, String gamemode, String teamType, Team team, JSONLocation spawn) {
        try {
            team.setSpawnLocations(List.of(LocationTranscriber.TranscribeFromJSON(spawn)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new JSONLocationException(title, gamemode, teamType + " spawn");
        }
    }

    public static Location TranscribeFromJSON(JSONLocation loc){
        return new Location(Bukkit.getWorld(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ());
    }
}
