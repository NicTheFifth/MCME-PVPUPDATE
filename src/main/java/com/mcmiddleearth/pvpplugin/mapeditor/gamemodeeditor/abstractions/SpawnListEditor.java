package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONSpawnListGamemode;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public abstract class SpawnListEditor extends GamemodeEditor {
    public void addSpawn(Player player){
        JSONLocation JSONSpawn = new JSONLocation(player.getLocation());
        ((JSONSpawnListGamemode)jsonGamemode).getSpawns().add(JSONSpawn);
        player.sendMessage(MiniMessage.miniMessage().deserialize(
                "<aqua>Spawn added to <gamemode>.</aqua>",
                Placeholder.parsed("gamemode", getDisplayString())
        ));
    }
    public void deleteSpawn(int toDelete, Player player){
        ((JSONSpawnListGamemode)jsonGamemode).getSpawns().remove(toDelete);
        player.sendMessage(MiniMessage.miniMessage().deserialize(
                "<aqua>Spawn removed from <gamemode>.</aqua>",
                Placeholder.parsed("gamemode", getDisplayString())
        ));
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
        player.sendMessage(MiniMessage.miniMessage().deserialize(
                """
                <aqua>Current selected gamemode: <gamemode>
                  Max players: <max>
                  pawns:<spawnnum></aqua>""",
                Placeholder.parsed("gamemode", getDisplayString()),
                Placeholder.parsed("max", String.valueOf(jsonGamemode.getMaximumPlayers())),
                Placeholder.parsed("spawnnum", String.valueOf(((JSONSpawnListGamemode)jsonGamemode).getSpawns().size())))
        );
    }
    public void teleportToSpawn(Player player, int index){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONSpawnListGamemode)jsonGamemode).getSpawns().get(index)
            ));
        player.sendMessage(MiniMessage.miniMessage().deserialize(
                "<aqua>Teleported to spawn <spawn>.</aqua>",
                Placeholder.parsed("spawn", String.valueOf(index))));
    }
}
