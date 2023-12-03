package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONCaptureTheFlag;
import org.bukkit.Location;

import java.util.List;

public class CaptureTheFlagEditor implements GamemodeEditor{
    JSONCaptureTheFlag jsonCaptureTheFlag;
    private CaptureTheFlagEditor(){}
    public CaptureTheFlagEditor(JSONMap map){
        this.jsonCaptureTheFlag = map.getJSONCaptureTheFlag();
    }
    public List<String> setBlueSpawn(Location blueSpawn){
        JSONLocation JSONBlueSpawn = new JSONLocation(blueSpawn);
        jsonCaptureTheFlag.setBlueSpawn(JSONBlueSpawn);
        return List.of(Style.INFO + "Blue spawn set for Capture the Flag.");
    }
    public List<String> setRedSpawn(Location redSpawn){
        JSONLocation JSONRedSpawn = new JSONLocation(redSpawn);
        jsonCaptureTheFlag.setRedSpawn(JSONRedSpawn);
        return List.of(Style.INFO + "Red spawn set for Capture the Flag.");
    }
    @Override
    public String[] setMaxPlayers(Integer maxPlayers) {
        jsonCaptureTheFlag.setMaximumPlayers(maxPlayers);
        return new String[]{String.format(Style.INFO + "Set the max players to %d.", maxPlayers)};
    }
    @Override
    public String getGamemode() {return "Capture the Flag";}
}
