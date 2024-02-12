package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONInfected;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.TeamSpawnEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class InfectedEditor extends TeamSpawnEditor {
    public InfectedEditor(JSONMap map){
        if(map.getJSONInfected() == null)
            map.setJSONInfected(new JSONInfected());
        setDisplayString("Infected");
        this.jsonGamemode = map.getJSONInfected();
        initSpawnNames();
    }
    public void setInfectedSpawn(Player player){
        Location infectedSpawn = player.getLocation();
        JSONLocation JSONInfectedSpawn = new JSONLocation(infectedSpawn);
        ((JSONInfected)jsonGamemode).setInfectedSpawn(JSONInfectedSpawn);
        sendBaseComponent(
            new ComponentBuilder("Infected spawn set for Infected.")
                .color(Style.INFO)
                .create(),
            player);
    }
    public void setSurvivorSpawn(Player player){
        Location survivorSpawn = player.getLocation();
        JSONLocation JSONSurvivorSpawn = new JSONLocation(survivorSpawn);
        ((JSONInfected)jsonGamemode).setSurvivorSpawn(JSONSurvivorSpawn);
        sendBaseComponent(
            new ComponentBuilder("Survivor spawn set for Infected.")
                .color(Style.INFO)
                .create(),
            player);
    }
    public String getGamemode(){return Gamemodes.INFECTED;}

    @Override
    public void sendStatus(Player player){
        sendBaseComponent(
            new ComponentBuilder("Current selected gamemode: Infected.")
                .color(Style.INFO)
                .append(String.format("  Max players: %d",
                    jsonGamemode.getMaximumPlayers()))
                .color(Style.INFO)
                .append(String.format("  Survivor spawn set: %b",
                    ((JSONInfected)jsonGamemode).getSurvivorSpawn()))
                .color(Style.INFO)
                .append(String.format("  Infected spawn set: %b",
                    ((JSONInfected)jsonGamemode).getInfectedSpawn()))
                .color(Style.INFO)
                .create(),
            player
        );
    }

    @Override
    protected void initSpawnNames() {
        getSpawnNames().put("infected", this::setInfectedSpawn);
        getSpawnNames().put("survivor", this::setSurvivorSpawn);
    }
}
