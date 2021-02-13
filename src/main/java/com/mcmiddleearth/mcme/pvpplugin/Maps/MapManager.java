package com.mcmiddleearth.mcme.pvpplugin.Maps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.mcmiddleearth.mcme.pvpplugin.exception.BadMapRegionException;
import com.mcmiddleearth.mcme.pvpplugin.json.JSONLocation;
import com.mcmiddleearth.mcme.pvpplugin.json.JSONMap;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MapManager {

    private final File mapDirectory;
    private final HashMap<String, JSONMap> maps = new HashMap<>();
    private final ObjectMapper objectMapper;

    public MapManager(File mapDirectory) {
        this.mapDirectory = mapDirectory;
        this.objectMapper = new ObjectMapper();

        load();
    }

    public static Region convertMapToRegion(JSONMap jsonMap) {
        List<String> worldIdentifiers = jsonMap.getRegionPoints().stream().map(JSONLocation::getWorld).collect(Collectors.toList());
        if (worldIdentifiers.size() != 1) {
            throw new BadMapRegionException(String.format("Multiple worlds have been defined %s", String.join(",",worldIdentifiers)));
        }

        World world = Bukkit.getWorld(worldIdentifiers.get(0));

        List<BlockVector2> worldEditPoints = jsonMap.getRegionPoints()
                .stream()
                .map(point -> BlockVector2.at(point.getX(), point.getZ()))
                .collect(Collectors.toList());

        return new Polygonal2DRegion(new BukkitWorld(world), worldEditPoints, 0, 1000);
    }

    public JSONMap getMap(String name) {
        return maps.get(name);
    }

    public void addMap(JSONMap jsonMap) {
        maps.put(jsonMap.getTitle(), jsonMap);
    }

    public void store() {
        JSONMap[] storeableMaps = this.maps.values().toArray(new JSONMap[]{});
        try {
            objectMapper.writeValue(new File(mapDirectory, "mapdata.json"), storeableMaps);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() {
        // Read File
        List<JSONMap> jsonMaps = new ArrayList<>();
        try {
            jsonMaps = Arrays.asList(objectMapper.readValue(new File(mapDirectory, "mapdata.json"), JSONMap[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        jsonMaps.forEach(map -> maps.put(map.getTitle(), map));
    }
}
