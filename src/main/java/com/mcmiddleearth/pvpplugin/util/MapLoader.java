package com.mcmiddleearth.pvpplugin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.exceptions.MapLoadException;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;

import java.io.File;
import java.util.Arrays;

public class MapLoader {

    public static void loadMaps() throws MapLoadException{
        ObjectMapper objectMapper = new ObjectMapper();
        File mapDirectory  = PVPPlugin.getInstance().getMapDirectory();
        Arrays.stream(mapDirectory.listFiles()).forEach(mapFile -> {
            try {
                JSONMap map = objectMapper.readValue(mapFile, JSONMap.class);
                PVPPlugin.getInstance().getMaps().put(map.getTitle(), map);
            } catch (Exception e) {
                new MapLoadException(e).printStackTrace();
            }
        });
    }

    public static void saveMaps(){
        ObjectMapper objectMapper = new ObjectMapper();
        File mapDirectory  = PVPPlugin.getInstance().getMapDirectory();
        PVPPlugin.getInstance().getMaps().values().forEach(jsonMap -> {
            try {
                File saveFile = new File(mapDirectory + System.getProperty(
                    "file.separator") + jsonMap.getTitle() + ".json");
                objectMapper.writeValue(saveFile, jsonMap);
            } catch (Exception e) {
                new MapLoadException(e).printStackTrace();
            }
        });
    }
}

