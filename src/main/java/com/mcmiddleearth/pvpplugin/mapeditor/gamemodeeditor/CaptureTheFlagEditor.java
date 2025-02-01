package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONCaptureTheFlag;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.SpecialPointEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CaptureTheFlagEditor extends RedBlueSpawnListEditor implements SpecialPointEditor {
    /**
     * SpecialPointNames is a map of &lt;name, setter of said point&gt;
     */
    MiniMessage mm = PVPPlugin.getInstance().getMiniMessage();
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
        player.sendMessage(mm.deserialize(
                "<aqua>Blue flag set for <title>.</aqua>",
                Placeholder.parsed("title", getDisplayString())));
    }
    public void teleportToBlueFlag(Player player){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONCaptureTheFlag)jsonGamemode).getBlueFlag()));
        player.sendMessage(mm.deserialize("<aqua>Teleported to the blue flag.</aqua>"));
    }
    public void setRedFlag(Player player){
        Location redFlag = player.getLocation();
        JSONLocation JSONRedFlag = new JSONLocation(redFlag);
        ((JSONCaptureTheFlag)jsonGamemode).setRedFlag(JSONRedFlag);
        player.sendMessage(mm.deserialize(
                "<aqua>Red flag set for <title>.</aqua>",
                Placeholder.parsed("title", getDisplayString())));
    }
    public void teleportToRedFlag(Player player){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONCaptureTheFlag)jsonGamemode).getRedFlag()));
        player.sendMessage(mm.deserialize("<aqua>Teleported to the blue flag.</aqua>"));
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
        JSONCaptureTheFlag ctf = (JSONCaptureTheFlag) jsonGamemode;
        player.sendMessage(mm.deserialize("""
                <aqua>Current selected gamemode: Capture the Flag.
                Max players: <max>
                Blue Spawns set: <bs>
                Blue flag set: <bf>
                Red spawns set: <rs
                Red flag set: <rf></aqua>
                """,
                Placeholder.parsed("max", String.valueOf(ctf.getMaximumPlayers())),
                Placeholder.parsed("bs", String.valueOf(ctf.getBlueSpawns().size())),
                Placeholder.parsed("bf", String.valueOf(ctf.getBlueFlag() == null)),
                Placeholder.parsed("rs", String.valueOf(ctf.getRedSpawns().size())),
                Placeholder.parsed("rf", String.valueOf(ctf.getRedFlag() == null))));
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
