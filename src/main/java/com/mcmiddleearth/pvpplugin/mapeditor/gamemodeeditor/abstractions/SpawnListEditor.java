package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONSpawnListGamemode;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    @Override
    public void ShowPoints(Player player) {
        AtomicInteger index = new AtomicInteger(0);
        List<JSONLocation> spawns =
            ((JSONSpawnListGamemode)jsonGamemode).getSpawns();
        spawns.forEach(spawn ->
            MapEditor.SpawnMarker(spawn, String.format("spawn %d",
            index.getAndIncrement())));
    }
    public void sendStatus(Player player){
        sendBaseComponent(
            new ComponentBuilder(String.format("Current selected gamemode: " +
                "%s", getDisplayString()))
                .color(Style.INFO)
                .append(String.format(" Max players: %d",
                    jsonGamemode.getMaximumPlayers()))
                .color(Style.INFO)
                .append(String.format("  Spawns: %d",
                    ((JSONSpawnListGamemode)jsonGamemode).getSpawns().size()))
                .color(Style.INFO)
                .create(),
            player
        );
    }
}
