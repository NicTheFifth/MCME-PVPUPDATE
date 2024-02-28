package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class TeamSpawnListEditor extends GamemodeEditor{
    protected Map<String, AddRemoveIndexTeleportQuartet> spawnListNames = new HashMap<>();
    protected abstract void initSpawnListNames();
    public Map<String, AddRemoveIndexTeleportQuartet> getSpawnListNames(){
        return spawnListNames;
    }

    public static class AddRemoveIndexTeleportQuartet {
        final private Consumer<Player> adder;
        final private BiConsumer<Player, Integer> deleter;
        final private Supplier<Integer> indexGetter;
        final private BiConsumer<Player, Integer> teleporter;
        public AddRemoveIndexTeleportQuartet(Consumer<Player> addSpawn,
                                             BiConsumer<Player, Integer> removeSpawn,
                                             Supplier<Integer> getIndex,
                                             BiConsumer<Player, Integer> teleporter){
            this.adder = addSpawn;
            this.deleter = removeSpawn;
            this.indexGetter = getIndex;
            this.teleporter = teleporter;
        }

        public void addSpawn(Player player){
            adder.accept(player);
        }
        public void deleteSpawn(Player player, Integer index){
            deleter.accept(player, index);
        }
        public Integer getIndex() {
            return indexGetter.get();
        }
        public void teleport(Player player, Integer index){
            teleporter.accept(player, index);
        }
    }
}
