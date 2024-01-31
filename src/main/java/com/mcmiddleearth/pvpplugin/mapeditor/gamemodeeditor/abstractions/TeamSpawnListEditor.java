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

    public static class AddRemoveIndexTrio{
        final public Consumer<Player> addSpawn;
        final public Function<Player, Consumer<Integer>> deleteSpawn;
        final public Supplier<Integer> getIndex;
        public AddRemoveIndexTrio(Consumer<Player> addSpawn, Function<Player,
            Consumer<Integer>> removeSpawn, Supplier<Integer> getIndex){
            this.addSpawn = addSpawn;
            this.deleteSpawn = removeSpawn;
            this.getIndex = getIndex;
        }
    }
}
