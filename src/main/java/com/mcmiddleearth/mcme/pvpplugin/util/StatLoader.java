package com.mcmiddleearth.mcme.pvpplugin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.exceptions.StatLoadException;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.Playerstat;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class StatLoader {

    public static void loadStats() throws StatLoadException {
        ObjectMapper objectMapper = new ObjectMapper();
        File statDirectory  = PVPPlugin.getInstance().getStatDirectory();
        Arrays.stream(Objects.requireNonNull(statDirectory.listFiles())).forEach(mapFile -> {
            try {
                Playerstat stat = objectMapper.readValue(mapFile, Playerstat.class);
                PVPPlugin.getInstance().getPlayerstats().put(UUID.fromString(mapFile.getName()), stat);
            } catch (Exception e) {
                throw new StatLoadException(e);
            }
        });
    }

    public static void saveStats(){
        ObjectMapper objectMapper = new ObjectMapper();
        File statDirectory  = PVPPlugin.getInstance().getStatDirectory();
        PVPPlugin.getInstance().getPlayerstats().forEach((uuid,stat) ->{
            try {
                File saveFile = new File(statDirectory + System.getProperty("file.separator") + uuid.toString());
                objectMapper.writeValue(saveFile, stat);
            } catch (Exception e) {
                throw new StatLoadException(e);
            }
        });
    }
}
