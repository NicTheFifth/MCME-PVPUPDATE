package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONCaptureTheFlag;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONRedBlueSpawnGamemode;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.SpecialPointEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class CaptureTheFlagEditor extends RedBlueSpawnEditor implements SpecialPointEditor {
    //SpecialPointNames is a map of <name, setter of said point>
    Map<String, Consumer<Player>> specialPointNames = new HashMap<>();
    private CaptureTheFlagEditor(){}
    public CaptureTheFlagEditor(JSONMap map){
        setDisplayString("Capture the Flag");
        if(map.getJSONCaptureTheFlag() == null)
            map.setJSONCaptureTheFlag(new JSONCaptureTheFlag());
        this.jsonGamemode = map.getJSONCaptureTheFlag();
        initSpecialPointNames();
    }
    public void setBlueFlag(Player player){
        Location blueFlag = player.getLocation();
        JSONLocation JSONBlueFlag = new JSONLocation(blueFlag);
        ((JSONCaptureTheFlag)jsonGamemode).setBlueFlag(JSONBlueFlag);
        sendBaseComponent(
            new ComponentBuilder(String.format("Blue flag set for %s.",
                getDisplayString()))
                .color(Style.INFO)
                .create(),
            player);
    }
    public void setRedFlag(Player player){
        Location redFlag = player.getLocation();
        JSONLocation JSONRedFlag = new JSONLocation(redFlag);
        ((JSONCaptureTheFlag)jsonGamemode).setRedFlag(JSONRedFlag);
        sendBaseComponent(
            new ComponentBuilder(String.format("Red flag set for %s.",
                getDisplayString()))
                .color(Style.INFO)
                .create(),
            player);
    }
    @Override
    public String getGamemode() {return Gamemodes.CAPTURETHEFLAG;}

    @Override
    public String[] getInfo(){
        return new String[]{
                String.format(Style.INFO + "Current selected gamemode: Capture the Flag."),
                String.format(Style.INFO + "Max players: %d",
                    jsonGamemode.getMaximumPlayers()),
                String.format(Style.INFO + "Blue spawn set: %b",
                    ((JSONRedBlueSpawnGamemode)jsonGamemode).getBlueSpawn()),
                String.format(Style.INFO + "Red spawn set: %b",
                    ((JSONRedBlueSpawnGamemode)jsonGamemode).getRedSpawn())
        };
    }
    @Override
    public void initSpecialPointNames() {
        getSpecialPointNames().put("blueflag", this::setBlueFlag);
        getSpecialPointNames().put("redflag", this::setRedFlag);
    }
    public Map<String, Consumer<Player>> getSpecialPointNames(){
        return specialPointNames;
    }
}
