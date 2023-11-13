package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import java.util.List;

public interface GamemodeEditor {
    List<String> setMaxPlayers(Integer maxPlayers);
    String getGamemode();
}
