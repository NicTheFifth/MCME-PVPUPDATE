package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONCaptureTheFlag;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONRedBlueSpawnGamemode;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONRedBlueSpawnListGamemode;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.SpecialPointEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class CaptureTheFlagEditor extends RedBlueSpawnListEditor implements SpecialPointEditor {
    /**
     * SpecialPointNames is a map of &lt;name, setter of said point&gt;
     */

    Map<String, SpecialPointEditor.SetterTeleporterPair> specialPointNames = new HashMap<>();
    public CaptureTheFlagEditor(JSONMap map){
        setDisplayString("Capture the Flag");
        if(map.getJSONCaptureTheFlag() == null)
            map.setJSONCaptureTheFlag(new JSONCaptureTheFlag());
        if(map.getJSONCaptureTheFlag().getRedSpawns() == null)
            map.getJSONCaptureTheFlag().setRedSpawns(new ArrayList<>());
        if(map.getJSONCaptureTheFlag().getBlueSpawns() == null)
            map.getJSONCaptureTheFlag().setBlueSpawns(new ArrayList<>());
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
    public void teleportToBlueFlag(Player player){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONCaptureTheFlag)jsonGamemode).getBlueFlag()));
        sendBaseComponent(
            new ComponentBuilder("Teleported to blueflag")
                .color(Style.INFO)
                .create(),
            player
        );
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
    public void teleportToRedFlag(Player player){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONCaptureTheFlag)jsonGamemode).getRedFlag()));
        sendBaseComponent(
            new ComponentBuilder("Teleported to redflag")
                .color(Style.INFO)
                .create(),
            player
        );
    }
    @Override
    public String getGamemode() {return Gamemodes.CAPTURETHEFLAG;}
    @Override
    public void ShowPoints(Player player) {
        JSONLocation redFlag = ((JSONCaptureTheFlag)jsonGamemode).getRedFlag();
        MapEditor.SpawnMarker(redFlag, "redflag");
        JSONLocation blueFlag =
            ((JSONCaptureTheFlag)jsonGamemode).getBlueFlag();
        MapEditor.SpawnMarker(blueFlag, "blueflag");
        super.ShowPoints(player);
    }
    @Override
    public void sendStatus(Player player){
        sendBaseComponent(
            new ComponentBuilder("Current selected gamemode: Capture the Flag.")
                .color(Style.INFO)
                .append(String.format("\nMax players: %d",
                    jsonGamemode.getMaximumPlayers()))
                .color(Style.INFO)
                .append(String.format("Blue spawns set: %d",
                    ((JSONRedBlueSpawnListGamemode)jsonGamemode).getBlueSpawns().size()))
                .color(Style.INFO)
                .append(String.format("Blue flag set: %b",
                            ((JSONCaptureTheFlag) jsonGamemode).getBlueFlag()))
                .color(Style.INFO)
                .append(String.format("Red spawns set: %d",
                    ((JSONRedBlueSpawnListGamemode)jsonGamemode).getRedSpawns().size()))
                .color(Style.INFO)
                .append(String.format("Red flag set: %b",
                    ((JSONCaptureTheFlag) jsonGamemode).getRedFlag()))
                .color(Style.INFO)
                .create(),
            player);
    }
    @Override
    public void initSpecialPointNames() {
        getSpecialPointNames().put("blueflag",
            new SpecialPointEditor.SetterTeleporterPair(this::setBlueFlag,
                this::teleportToBlueFlag));
        getSpecialPointNames().put("redflag",
            new SpecialPointEditor.SetterTeleporterPair(this::setRedFlag,
                this::teleportToRedFlag));
    }
    public Map<String, SpecialPointEditor.SetterTeleporterPair> getSpecialPointNames(){
        return specialPointNames;
    }
}
