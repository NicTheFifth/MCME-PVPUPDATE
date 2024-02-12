package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONRedBlueSpawnGamemode;
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
        getSpawnNames().put("red", this::setRedSpawn);
        getSpawnNames().put("blue", this::setBlueSpawn);
    }
}
