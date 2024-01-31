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
    private String displayString;

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
    public void setMaxPlayers(Integer maxPlayers, Player player) {
        jsonGamemode.setMaximumPlayers(maxPlayers);
        sendBaseComponent(
            new ComponentBuilder(String.format("Set the max players to %d.",
                maxPlayers))
                .color(Style.INFO)
                .create(),
            player);
    }
    public static String BlueSpawn(){
        return "blue";
    }
    public static String RedSpawn(){
        return "red";
    }

    protected void setDisplayString(String displayString){
        this.displayString = displayString;
    }
    protected String getDisplayString(){
        return this.displayString;
    }
    @Override
    public String[] getInfo() {
        return new String[0];
    }
    protected void initSpawnNames() {
        getSpawnNames().put("red", this::setBlueSpawn);
        getSpawnNames().put("blue", this::setBlueSpawn);
    }
}
