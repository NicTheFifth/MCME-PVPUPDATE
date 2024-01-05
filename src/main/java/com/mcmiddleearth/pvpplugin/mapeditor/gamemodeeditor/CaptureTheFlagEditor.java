package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONCaptureTheFlag;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class CaptureTheFlagEditor implements GamemodeEditor, RedBlueTeamEditor {
    JSONCaptureTheFlag jsonCaptureTheFlag;
    private CaptureTheFlagEditor(){}
    public CaptureTheFlagEditor(JSONMap map){
        if(map.getJSONCaptureTheFlag() == null)
            map.setJSONCaptureTheFlag(new JSONCaptureTheFlag());
        this.jsonCaptureTheFlag = map.getJSONCaptureTheFlag();

    }
    public void setBlueSpawn(Player player){
        Location blueSpawn = player.getLocation();
        JSONLocation JSONBlueSpawn = new JSONLocation(blueSpawn);
        jsonCaptureTheFlag.setBlueSpawn(JSONBlueSpawn);
        sendBaseComponent(
            new ComponentBuilder("Blue spawn set for Capture the Flag.")
                .color(Style.INFO)
                .create(),
            player);
    }
    public void setRedSpawn(Player player){
        Location redSpawn = player.getLocation();
        JSONLocation JSONRedSpawn = new JSONLocation(redSpawn);
        jsonCaptureTheFlag.setRedSpawn(JSONRedSpawn);
        sendBaseComponent(
            new ComponentBuilder("Red spawn set for Capture the Flag.")
                .color(Style.INFO)
                .create(),
            player);
    }
    @Override
    public void setMaxPlayers(Integer maxPlayers, Player player) {
        jsonCaptureTheFlag.setMaximumPlayers(maxPlayers);
        sendBaseComponent(
            new ComponentBuilder(String.format("Set the max players to %d.",
                maxPlayers))
                .color(Style.INFO)
                .create(),
            player);
    }
    @Override
    public String getGamemode() {return "Capture the Flag";}

    @Override
    public void setMap(JSONMap map) {
        if(map.getJSONCaptureTheFlag() == null)
            map.setJSONCaptureTheFlag(new JSONCaptureTheFlag());
        this.jsonCaptureTheFlag = map.getJSONCaptureTheFlag();
    }

    @Override
    public String[] getInfo(){
        return new String[]{
                String.format(Style.INFO + "Current selected gamemode: Capture the Flag."),
                String.format(Style.INFO + "Max players: %d", jsonCaptureTheFlag.getMaximumPlayers()),
                String.format(Style.INFO + "Blue spawn set: %b", jsonCaptureTheFlag.getBlueSpawn()),
                String.format(Style.INFO + "Red spawn set: %b", jsonCaptureTheFlag.getRedSpawn())
        };
    }
}
