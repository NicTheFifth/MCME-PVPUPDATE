package com.mcmiddleearth.mcme.pvpplugin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.exceptions.MapLoadException;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class MapLoader {

    PVPPlugin pvpPlugin;
    public MapLoader(PVPPlugin pvpPlugin)
    {
        this.pvpPlugin = pvpPlugin;
    }

    public void loadMaps() throws MapLoadException{
        ObjectMapper objectMapper = new ObjectMapper();
        File mapDirectory  = pvpPlugin.getMapDirectory();
        Arrays.stream(Objects.requireNonNull(mapDirectory.listFiles())).forEach(mapFile -> {
            try {
                JSONMap map = objectMapper.readValue(mapFile, JSONMap.class);
                pvpPlugin.getMaps().put(map.getAbbreviation(), map);
            } catch (Exception e) {
                throw new MapLoadException(e);
            }
        });
    }

    public void saveMaps(){
        ObjectMapper objectMapper = new ObjectMapper();
        File mapDirectory  = pvpPlugin.getMapDirectory();
        pvpPlugin.getMaps().values().forEach(jsonMap -> {
            try {
                File saveFile = new File(mapDirectory + System.getProperty("file.separator") + jsonMap.getTitle());
                objectMapper.writeValue(saveFile, jsonMap);
            } catch (Exception e) {
                throw new MapLoadException(e);
            }
        });
    }
}

