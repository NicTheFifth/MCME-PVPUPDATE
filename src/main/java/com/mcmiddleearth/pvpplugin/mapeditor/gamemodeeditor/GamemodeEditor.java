package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import org.bukkit.entity.Player;

public interface GamemodeEditor {
    void setMaxPlayers(Integer maxPlayers, Player player);
    String getGamemode();
    void setMap(JSONMap map);
    String[] getInfo();
}
