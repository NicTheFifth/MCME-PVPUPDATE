package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.Consumer;

public interface SpecialPointEditor {
    void initSpecialPointNames();
    Map<String, SetterTeleporterPair> getSpecialPointNames();

    class SetterTeleporterPair{
        final Consumer<Player> setter;
        final Consumer<Player> teleporter;

        public SetterTeleporterPair(Consumer<Player> setter,
                                    Consumer<Player> teleporter){
            this.setter = setter;
            this.teleporter = teleporter;
        }
        public void setPoint(Player player) {
            setter.accept(player);
        }
        public void Teleport(Player player){
            teleporter.accept(player);
        }
    }
}
