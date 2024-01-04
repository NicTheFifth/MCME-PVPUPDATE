package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONRingBearer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class RingBearerEditor implements GamemodeEditor{
    JSONRingBearer jsonRingBearer;
    private RingBearerEditor(){}
    public RingBearerEditor(JSONMap map){
        if(map.getJSONRingBearer() == null)
            map.setJSONRingBearer(new JSONRingBearer());
        jsonRingBearer = map.getJSONRingBearer();
    }
    public List<String> setBlueSpawn(Location blueSpawn){
        JSONLocation JSONBlueSpawn = new JSONLocation(blueSpawn);
        jsonRingBearer.setBlueSpawn(JSONBlueSpawn);
        return List.of(Style.INFO + "Blue spawn set for Ringbearer.");
    }
    public List<String> setRedSpawn(Location redSpawn){
        JSONLocation JSONRedSpawn = new JSONLocation(redSpawn);
        jsonRingBearer.setRedSpawn(JSONRedSpawn);
        return List.of(Style.INFO + "Red spawn set for Ringbearer.");
    }
    @Override
    public void setMaxPlayers(Integer maxPlayers, Player player) {
        jsonRingBearer.setMaximumPlayers(maxPlayers);
        sendBaseComponent(
            new ComponentBuilder(String.format("Set the max players to %d.",
                maxPlayers))
                .color(Style.INFO)
                .create(),
            player);
    }
    @Override
    public String getGamemode() {return "Ringbearer";}

    @Override
    public void setMap(JSONMap map) {
        if(map.getJSONRingBearer() == null)
            map.setJSONRingBearer(new JSONRingBearer());
        jsonRingBearer = map.getJSONRingBearer();
    }

    @Override
    public String[] getInfo(){
        return new String[]{
                String.format(Style.INFO + "Current selected gamemode: Ringbearer."),
                String.format(Style.INFO + "Max players: %d", jsonRingBearer.getMaximumPlayers()),
                String.format(Style.INFO + "Blue spawn set: %b", jsonRingBearer.getBlueSpawn()),
                String.format(Style.INFO + "Red spawn set: %b", jsonRingBearer.getRedSpawn())
        };
    }
}
