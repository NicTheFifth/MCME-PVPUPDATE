package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONRedBlueSpawnListGamemode;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public abstract class RedBlueSpawnListEditor extends GamemodeEditor{

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
    public void DeleteBlueSpawn(int toDelete, Player player){
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
    public static String[] getSpawns(){
        return new String[]{BlueSpawn(), RedSpawn()};
    }
    public static String BlueSpawn(){
        return "blue";
    }
    public static String RedSpawn(){
        return "red";
    }
    public Integer amountOfBlueSpawns(){
        return ((JSONRedBlueSpawnListGamemode)jsonGamemode).getBlueSpawns().size();
    }
    public Integer amountOfRedSpawns(){
        return ((JSONRedBlueSpawnListGamemode)jsonGamemode).getRedSpawns().size();
    }
}
