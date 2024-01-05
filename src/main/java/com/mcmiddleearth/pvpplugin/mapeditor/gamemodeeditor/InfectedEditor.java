package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONInfected;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class InfectedEditor implements GamemodeEditor{
    JSONInfected jsonInfected;
    private InfectedEditor(){}
    public InfectedEditor(JSONMap map){
        if(map.getJSONInfected() == null)
            map.setJSONInfected(new JSONInfected());
        this.jsonInfected = map.getJSONInfected();
    }
    public void setInfectedSpawn(Player player){
        Location infectedSpawn = player.getLocation();
        JSONLocation JSONInfectedSpawn = new JSONLocation(infectedSpawn);
        jsonInfected.setInfectedSpawn(JSONInfectedSpawn);
        sendBaseComponent(
            new ComponentBuilder("Infected spawn set for Infected.")
                .color(Style.INFO)
                .create(),
            player);
    }
    public void setSurvivorSpawn(Player player){
        Location survivorSpawn = player.getLocation();
        JSONLocation JSONSurvivorSpawn = new JSONLocation(survivorSpawn);
        jsonInfected.setSurvivorSpawn(JSONSurvivorSpawn);
        sendBaseComponent(
            new ComponentBuilder("Survivor spawn set for Infected.")
                .color(Style.INFO)
                .create(),
            player);
    }
    public void setMaxPlayers(Integer maxPlayers, Player player) {
        jsonInfected.setMaximumPlayers(maxPlayers);
        sendBaseComponent(
            new ComponentBuilder(String.format("Set the max players to %d.",
                maxPlayers))
                .color(Style.INFO)
                .create(),
            player);
    }
    public String getGamemode(){return Gamemodes.INFECTED;}

    @Override
    public void setMap(JSONMap map) {
        if(map.getJSONInfected() == null)
            map.setJSONInfected(new JSONInfected());
        this.jsonInfected = map.getJSONInfected();
    }

    @Override
    public String[] getInfo(){
        return new String[]{
                String.format(Style.INFO + "Current selected gamemode: Infected."),
                String.format(Style.INFO + "Max players: %d", jsonInfected.getMaximumPlayers()),
                String.format(Style.INFO + "Survivor spawn set: %b", jsonInfected.getSurvivorSpawn()),
                String.format(Style.INFO + "Infected spawn set: %b", jsonInfected.getInfectedSpawn())
        };
    }
}
