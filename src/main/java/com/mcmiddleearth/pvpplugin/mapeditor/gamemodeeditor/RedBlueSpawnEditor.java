package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONRedBlueSpawnGamemode;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.TeamSpawnEditor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class RedBlueSpawnEditor extends TeamSpawnEditor {

    protected RedBlueSpawnEditor(){
        initSpawnNames();
    }

    public void setBlueSpawn(Player player){
        Location blueSpawn = player.getLocation();
        JSONLocation JSONBlueSpawn = new JSONLocation(blueSpawn);
        ((JSONRedBlueSpawnGamemode)jsonGamemode).setBlueSpawn(JSONBlueSpawn);
        sendBaseComponent(
            new ComponentBuilder(String.format("Blue spawn set for %s.",
                getDisplayString()))
                .color(Style.INFO)
                .create(),
            player);
    }
    public void teleportToBlueSpawn(Player player){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONRedBlueSpawnGamemode)jsonGamemode).getBlueSpawn()));
        sendBaseComponent(
            new ComponentBuilder("Teleported to blue spawn")
                .color(Style.INFO)
                .create(),
            player
        );
    }
    public void setRedSpawn(Player player){
        Location redSpawn = player.getLocation();
        JSONLocation JSONRedSpawn = new JSONLocation(redSpawn);
        ((JSONRedBlueSpawnGamemode)jsonGamemode).setRedSpawn(JSONRedSpawn);
        sendBaseComponent(
            new ComponentBuilder(String.format("Red spawn set for %s.",
                getDisplayString()))
                .color(Style.INFO)
                .create(),
            player);
    }
    public void teleportToRedSpawn(Player player){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONRedBlueSpawnGamemode)jsonGamemode).getRedSpawn()));
        sendBaseComponent(
            new ComponentBuilder("Teleported to red spawn")
                .color(Style.INFO)
                .create(),
            player
        );
    }
    @Override
    public void ShowPoints(Player player) {
        JSONLocation redSpawn = ((JSONRedBlueSpawnGamemode)jsonGamemode)
            .getRedSpawn();
        MapEditor.SpawnMarker(redSpawn, "spawn red");
        JSONLocation blueSpawn =
            ((JSONRedBlueSpawnGamemode)jsonGamemode).getBlueSpawn();
        MapEditor.SpawnMarker(blueSpawn, "spawn blue");

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
                .append(String.format("  Blue spawn set: %b",
                    ((JSONRedBlueSpawnGamemode)jsonGamemode).getBlueSpawn()))
                .color(Style.INFO)
                .append(String.format("  Red spawn set: %b",
                    ((JSONRedBlueSpawnGamemode)jsonGamemode).getRedSpawn()))
                .color(Style.INFO)
                .create(),
            player
        );
    }
    protected void initSpawnNames() {
        getSpawnNames().put("red",
            new SetterTeleporterPair(this::setRedSpawn,
                this:: teleportToRedSpawn));
        getSpawnNames().put("blue",
            new SetterTeleporterPair(this::setBlueSpawn,
                this::teleportToBlueSpawn));
    }
}
