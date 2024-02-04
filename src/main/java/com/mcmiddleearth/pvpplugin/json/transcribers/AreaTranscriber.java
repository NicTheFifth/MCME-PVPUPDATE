
package com.mcmiddleearth.pvpplugin.json.transcribers;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class AreaTranscriber {
    public static Polygonal2DRegion TranscribeArea(JSONMap map){
        List<JSONLocation> regionPoints = map.getRegionPoints();
        ArrayList<BlockVector2> wePoints = new ArrayList<>();
        World world = Bukkit.getWorld("world");
        for (JSONLocation e : regionPoints) {
            BlockVector2 point = BlockVector2.at(e.getX(), e.getZ());
            wePoints.add(point);
        }
        return new Polygonal2DRegion(new BukkitWorld(world), wePoints, 0, 1000);
    }
}

