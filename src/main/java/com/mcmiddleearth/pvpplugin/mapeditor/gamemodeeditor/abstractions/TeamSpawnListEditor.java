package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class TeamSpawnListEditor extends GamemodeEditor{
    protected Map<String, AddRemoveIndexTrio> spawnListNames = new HashMap<>();
    protected abstract void initSpawnListNames();
    public Map<String, AddRemoveIndexTrio> getSpawnListNames(){
        return spawnListNames;
    }

    protected class AddRemoveIndexTrio{
        final Consumer<Player> addSpawn;
        final Function<Player, Consumer<Integer>> removeSpawn;
        final Supplier<Integer> getIndex;
        public AddRemoveIndexTrio(Consumer<Player> addSpawn, Function<Player,
            Consumer<Integer>> removeSpawn, Supplier<Integer> getIndex){
            this.addSpawn = addSpawn;
            this.removeSpawn = removeSpawn;
            this.getIndex = getIndex;
        }
    }
}
