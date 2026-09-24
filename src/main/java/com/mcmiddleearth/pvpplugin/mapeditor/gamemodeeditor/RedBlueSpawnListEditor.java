package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONRedBlueSpawnListGamemode;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.TeamSpawnListEditor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class RedBlueSpawnListEditor extends TeamSpawnListEditor {

    protected RedBlueSpawnListEditor(){
        initSpawnListNames();
    }
    MiniMessage mm = PVPPlugin.getInstance().getMiniMessage();

    public void AddBlueSpawn(Player player) {
        Location blueSpawn = player.getLocation();
        JSONLocation JSONBlueSpawn = new JSONLocation(blueSpawn);
        ((JSONRedBlueSpawnListGamemode) jsonGamemode).getBlueSpawns().add(JSONBlueSpawn);
        player.sendMessage(mm.deserialize("<aqua>Blue spawn added to <title>.</aqua>",
                Placeholder.parsed("title", getDisplayString())));
    }

    public void DeleteBlueSpawn(Player player, int toDelete){
        ((JSONRedBlueSpawnListGamemode)jsonGamemode).getBlueSpawns().remove(toDelete);
        player.sendMessage(mm.deserialize("<aqua>Blue spawn removed from <title>.</aqua>",
                Placeholder.parsed("title", getDisplayString())));
    }

    public void teleportToBlueSpawn(Player player, int index){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONRedBlueSpawnListGamemode)jsonGamemode).getBlueSpawns().get(index)
            ));
        player.sendMessage(mm.deserialize("<aqua>Teleported to blue spawn <index>.</aqua>",
                Placeholder.parsed("index", String.valueOf(index))));
    }

    public void AddRedSpawn(Player player){
        Location redSpawn = player.getLocation();
        JSONLocation JSONRedSpawn = new JSONLocation(redSpawn);
        ((JSONRedBlueSpawnListGamemode)jsonGamemode).getRedSpawns().add(JSONRedSpawn);
        player.sendMessage(mm.deserialize("<aqua>Red spawn added to <title>.</aqua>",
                Placeholder.parsed("title", getDisplayString())));
    }

    public void DeleteRedSpawn(Player player, int toDelete){
        ((JSONRedBlueSpawnListGamemode)jsonGamemode).getRedSpawns().remove(toDelete);
        player.sendMessage(mm.deserialize("<aqua>Red spawn removed from <title>.</aqua>",
                Placeholder.parsed("title", getDisplayString())));
    }

    public void teleportToRedSpawn(Player player, int index){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONRedBlueSpawnListGamemode)jsonGamemode).getRedSpawns().get(index)
            ));
        player.sendMessage(mm.deserialize("<aqua>Teleported to red spawn <index>.</aqua>",
                Placeholder.parsed("index", String.valueOf(index))));
    }

    public Integer amountOfBlueSpawns(){
        return ((JSONRedBlueSpawnListGamemode)jsonGamemode).getBlueSpawns().size();
    }

    public Integer amountOfRedSpawns(){
        return ((JSONRedBlueSpawnListGamemode)jsonGamemode).getRedSpawns().size();
    }

    @Override
    public void ShowPoints(Player player) {
        AtomicInteger index = new AtomicInteger(0);
        List<JSONLocation> redSpawns =
            ((JSONRedBlueSpawnListGamemode)jsonGamemode).getRedSpawns();
        redSpawns.forEach(spawn ->
            MapEditor.SpawnMarker(spawn, String.format("spawn red %d",
                index.getAndIncrement()))
        );
        index.set(0);
        List<JSONLocation> blueSpawns =
            ((JSONRedBlueSpawnListGamemode)jsonGamemode).getBlueSpawns();
        blueSpawns.forEach(spawn ->
            MapEditor.SpawnMarker(spawn, String.format("spawn blue %d",
                index.getAndIncrement())));
    }

    @Override
    public void sendStatus(Player player) {
        player.sendMessage(mm.deserialize("""
                <aqua>Current selected gamemode: <title>
                  Max players : <max>
                  Blue spawns : <bs>
                  Red spawns : <rs></aqua>""",
                Placeholder.parsed("title", getDisplayString()),
                Placeholder.parsed("max", String.valueOf(jsonGamemode.getMaximumPlayers())),
                Placeholder.parsed("bs", String.valueOf(amountOfBlueSpawns())),
                Placeholder.parsed("rs", String.valueOf(amountOfRedSpawns()))));
    }

    protected void initSpawnListNames() {
        getSpawnListNames().put("blue",
            new AddRemoveIndexTeleportQuartet(
                this::AddBlueSpawn,
                this::DeleteBlueSpawn,
                this::amountOfBlueSpawns,
                this::teleportToBlueSpawn));
        getSpawnListNames().put("red",
            new AddRemoveIndexTeleportQuartet(
                this::AddRedSpawn,
                this::DeleteRedSpawn,
                this::amountOfRedSpawns,
                this::teleportToRedSpawn
            ));
    }
}
