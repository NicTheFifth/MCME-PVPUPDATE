package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions;

import org.bukkit.entity.Player;

public interface SpawnListEditor {
    void addSpawn(Player player);
    void deleteSpawn(int toDelete, Player player);
    Integer amountOfSpawns();
}
