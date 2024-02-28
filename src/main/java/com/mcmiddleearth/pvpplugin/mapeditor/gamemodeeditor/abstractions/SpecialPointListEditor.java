package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface SpecialPointListEditor {
    void initSpecialPointListNames();
    Map<String, AddRemoveIndexTeleportQuartet> getSpecialPointListNames();
    class AddRemoveIndexTeleportQuartet {
        final Consumer<Player> adder;
        final BiConsumer<Player, Integer> deleter;
        final Supplier<Integer> indexGetter;
        final BiConsumer<Player, Integer> teleporter;
        public AddRemoveIndexTeleportQuartet(Consumer<Player> addPoint,
                                             BiConsumer<Player, Integer> removePoint,
                                             Supplier<Integer> getIndex,
                                             BiConsumer<Player, Integer> teleporter){
            this.adder = addPoint;
            this.deleter = removePoint;
            this.indexGetter = getIndex;
            this.teleporter = teleporter;
        }

        public void addPoint(Player player) {
            adder.accept(player);
        }
        public void deletePoint(Player player, Integer index){
            deleter.accept(player, index);
        }
        public Integer getIndex(){
            return indexGetter.get();
        }
        public void teleport(Player player, Integer index){
            teleporter.accept(player, index);
        }
    }
}
