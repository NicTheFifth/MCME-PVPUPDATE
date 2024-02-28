package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class TeamSpawnEditor extends GamemodeEditor{
    //SpawnNames is a map of <name, setter of said spawn>
    protected Map<String, SetterTeleporterPair> spawnNames =
        new HashMap<>();
    protected abstract void initSpawnNames();
    public Map<String, SetterTeleporterPair> getSpawnNames(){
        return spawnNames;
    }

    public static class SetterTeleporterPair{
        final Consumer<Player> setter;
        final Consumer<Player> teleporter;

        public SetterTeleporterPair(Consumer<Player> setter,
                                    Consumer<Player> teleporter){
            this.setter = setter;
            this.teleporter = teleporter;
        }

        public void setSpawn(Player player){
            setter.accept(player);
        }
        public void Teleport(Player player){
            teleporter.accept(player);
        }
    }
}
