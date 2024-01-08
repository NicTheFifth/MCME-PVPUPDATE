package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions;

import org.bukkit.entity.Player;

public interface GamemodeEditor {
    void setMaxPlayers(Integer maxPlayers, Player player);
    String getGamemode();
    String[] getInfo();
}
