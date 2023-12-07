package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONRingBearer;
import org.bukkit.Location;

import java.util.List;

public class RingBearerEditor implements GamemodeEditor{
    JSONRingBearer jsonRingBearer;
    private RingBearerEditor(){}
    public RingBearerEditor(JSONMap map){
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
    public String[] setMaxPlayers(Integer maxPlayers) {
        jsonRingBearer.setMaximumPlayers(maxPlayers);
        return new String[]{String.format(Style.INFO + "Set the max players to %d.", maxPlayers)};
    }
    @Override
    public String getGamemode() {return "Ringbearer";}
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
