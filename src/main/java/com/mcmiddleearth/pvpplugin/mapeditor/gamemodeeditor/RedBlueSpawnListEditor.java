package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONRedBlueSpawnListGamemode;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.TeamSpawnListEditor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
    public void DeleteRedSpawn(int toDelete, Player player){
        ((JSONRedBlueSpawnListGamemode)jsonGamemode).getRedSpawns().remove(toDelete);
        sendBaseComponent(new ComponentBuilder(String.format("Red spawn " +
                "removed from %s.", getDisplayString()))
                .color(Style.INFO)
                .create(),
            player);
    }
    public Integer amountOfBlueSpawns(){
        return ((JSONRedBlueSpawnListGamemode)jsonGamemode).getBlueSpawns().size();
    }
    public Integer amountOfRedSpawns(){
        return ((JSONRedBlueSpawnListGamemode)jsonGamemode).getRedSpawns().size();
    }
    @Override
    public String[] getInfo() {
        return new String[0];
    }
    protected void initSpawnListNames() {
        getSpawnListNames().put("blue",
            new AddRemoveIndexTrio(
                this::AddBlueSpawn,
                player->index->DeleteBlueSpawn(player,index),
                this::amountOfBlueSpawns));
        getSpawnListNames().put("red",
            new AddRemoveIndexTrio(
                this::AddRedSpawn,
                player -> index -> DeleteBlueSpawn(player,index),
                this::amountOfRedSpawns
            ));
    }
}
