package com.mcmiddleearth.mcme.pvpplugin.json.transcribers;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationTranscriber {
    public static Location TranscribeFromJSON(JSONLocation loc){
        return new Location(Bukkit.getWorld(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ());
    }
}
