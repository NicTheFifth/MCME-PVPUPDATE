package com.mcmiddleearth.mcme.pvpplugin.json.transcribers;

import com.mcmiddleearth.mcme.pvpplugin.exceptions.BadRegionException;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes.BaseRunner;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class AreaTranscriber {
    public static void TranscribeArea(String mapName, List<JSONLocation> regionPoints, BaseRunner runner) {
        ArrayList<BlockVector2> wePoints = new ArrayList<>();
        World world = Bukkit.getWorld("world");
        try {
            for (JSONLocation e : regionPoints) {
                BlockVector2 point = BlockVector2.at(e.getX(), e.getZ());
                wePoints.add(point);

                runner.setRegion(new Polygonal2DRegion(new BukkitWorld(world), wePoints, 0, 1000));
            }
        } catch (Exception e) {
            throw new BadRegionException(mapName);
        }
    }
}
