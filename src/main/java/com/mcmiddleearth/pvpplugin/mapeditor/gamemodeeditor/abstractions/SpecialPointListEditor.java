package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface SpecialPointListEditor {
    Map<String, AddRemoveIndexTrio> specialPointListNames =
        new HashMap<>();
    void initSpecialPointListNames();
    default Map<String, AddRemoveIndexTrio> getSpecialPointListNames(){
        return specialPointListNames;
    }
    class AddRemoveIndexTrio{
        final public Consumer<Player> addPoint;
        final public Function<Player, Consumer<Integer>> deletePoint;
        final public Supplier<Integer> getIndex;
        public AddRemoveIndexTrio(Consumer<Player> addPoint, Function<Player,
            Consumer<Integer>> removePoint, Supplier<Integer> getIndex){
            this.addPoint = addPoint;
            this.deletePoint = removePoint;
            this.getIndex = getIndex;
        }
    }
}
