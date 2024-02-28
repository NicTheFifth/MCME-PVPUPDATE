package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONRedBlueSpawnListGamemode;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.TeamSpawnListEditor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class RedBlueSpawnListEditor extends TeamSpawnListEditor {

    protected RedBlueSpawnListEditor(){
        initSpawnListNames();
    }

    public void AddBlueSpawn(Player player) {
        Location blueSpawn = player.getLocation();
        JSONLocation JSONBlueSpawn = new JSONLocation(blueSpawn);
        ((JSONRedBlueSpawnListGamemode) jsonGamemode).getBlueSpawns().add(JSONBlueSpawn);
        sendBaseComponent(new ComponentBuilder(String.format("Blue spawn " +
                "added to %s.", getDisplayString()))
                .color(Style.INFO)
                .create(),
            player);
    }
    public void DeleteBlueSpawn(Player player, int toDelete){
        ((JSONRedBlueSpawnListGamemode)jsonGamemode).getBlueSpawns().remove(toDelete);
        sendBaseComponent(new ComponentBuilder(String.format("Blue spawn " +
                "removed from %s.", getDisplayString()))
                .color(Style.INFO)
                .create(),
            player);
    }
    public void teleportToBlueSpawn(Player player, int index){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONRedBlueSpawnListGamemode)jsonGamemode).getBlueSpawns().get(index)
            ));
        sendBaseComponent(
            new ComponentBuilder(String.format("Teleported to blue spawn %d",
                index))
                .color(Style.INFO)
                .create(),
            player
        );
    }
    public void AddRedSpawn(Player player){
        Location redSpawn = player.getLocation();
        JSONLocation JSONRedSpawn = new JSONLocation(redSpawn);
        ((JSONRedBlueSpawnListGamemode)jsonGamemode).getRedSpawns().add(JSONRedSpawn);
        sendBaseComponent(new ComponentBuilder(String.format("Red spawn added" +
                " to %s.", getDisplayString()))
                .color(Style.INFO)
                .create(),
            player);
    }
    public void DeleteRedSpawn(Player player, int toDelete){
        ((JSONRedBlueSpawnListGamemode)jsonGamemode).getRedSpawns().remove(toDelete);
        sendBaseComponent(new ComponentBuilder(String.format("Red spawn " +
                "removed from %s.", getDisplayString()))
                .color(Style.INFO)
                .create(),
            player);
    }
    public void teleportToRedSpawn(Player player, int index){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONRedBlueSpawnListGamemode)jsonGamemode).getRedSpawns().get(index)
            ));
        sendBaseComponent(
            new ComponentBuilder(String.format("Teleported to red spawn %d",
                index))
                .color(Style.INFO)
                .create(),
            player
        );
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
        sendBaseComponent(
            new ComponentBuilder(
                String.format("Current selected gamemode: %s.",
                    getDisplayString()))
                .color(Style.INFO)
                .append(String.format("  Max players: %d",
                    jsonGamemode.getMaximumPlayers()))
                .color(Style.INFO)
                .append(String.format("  Blue spawns: %d",
                    amountOfBlueSpawns()))
                .color(Style.INFO)
                .append(String.format("  Red spawns: %d",
                    amountOfRedSpawns()))
                .color(Style.INFO)
                .create(),
            player
        );
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
