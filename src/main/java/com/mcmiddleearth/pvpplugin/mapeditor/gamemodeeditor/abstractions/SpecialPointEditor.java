package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public interface SpecialPointEditor {
    void initSpecialPointNames();
    Map<String, Consumer<Player>> getSpecialPointNames();
}
