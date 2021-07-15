package com.mcmiddleearth.mcme.pvpplugin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.exceptions.MapLoadException;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.Playerstat;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class StatLoader {
    //TODO: make the StatLoader correct
    PVPPlugin pvpPlugin;
    public StatLoader(PVPPlugin pvpPlugin)
    {
        this.pvpPlugin = pvpPlugin;
    }

    public void loadMaps() throws MapLoadException {
        ObjectMapper objectMapper = new ObjectMapper();
        File statDirectory  = pvpPlugin.getStatDirectory();
        Arrays.stream(Objects.requireNonNull(statDirectory.listFiles())).forEach(mapFile -> {
            try {
                Playerstat stat = objectMapper.readValue(mapFile, Playerstat.class);
                pvpPlugin.getPlayerstats().put(UUID.fromString(mapFile.getName()), stat);
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
