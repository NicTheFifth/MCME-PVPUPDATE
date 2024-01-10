package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONSpawnListGamemode;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public abstract class SpawnListEditor extends GamemodeEditor {
    public void addSpawn(Player player){
        JSONLocation JSONSpawn = new JSONLocation(player.getLocation());
        ((JSONSpawnListGamemode)jsonGamemode).getSpawns().add(JSONSpawn);
        sendBaseComponent(new ComponentBuilder(String.format("Spawn added to " +
                "%s.", getDisplayString()))
                .color(Style.INFO)
                .create(),
            player);

    }
    public void deleteSpawn(int toDelete, Player player){
        ((JSONSpawnListGamemode)jsonGamemode).getSpawns().remove(toDelete);
        sendBaseComponent(
            new ComponentBuilder(String.format("Spawn removed from %s.",
                getDisplayString()))
                .color(Style.INFO)
                .create(),
            player);
    }
    public Integer amountOfSpawns(){
        return ((JSONSpawnListGamemode)jsonGamemode).getSpawns().size();
    }
}
