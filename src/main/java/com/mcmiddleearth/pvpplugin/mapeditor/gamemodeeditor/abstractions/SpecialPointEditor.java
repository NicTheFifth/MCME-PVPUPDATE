package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public interface SpecialPointEditor {
    //SpecialPointNames is a map of <name, setter of said point>
    Map<String, Consumer<Player>> specialPointNames = new HashMap<>();
    void initSpecialPointNames();
    default Map<String, Consumer<Player>> getSpecialPointNames(){
        return specialPointNames;
    }
}
