package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class TeamSpawnEditor extends GamemodeEditor{
    //SpawnNames is a map of <name, setter of said spawn>
    protected Map<String, Consumer<Player>> spawnNames = new HashMap<>();
    protected abstract void initSpawnNames();
    public Map<String, Consumer<Player>> getSpawnNames(){
        return spawnNames;
    }
}
