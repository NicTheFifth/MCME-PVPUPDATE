package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONCaptureTheFlag;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.RedBlueSpawnEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class CaptureTheFlagEditor extends RedBlueSpawnEditor{
    JSONCaptureTheFlag jsonCaptureTheFlag;
    private CaptureTheFlagEditor(){}
    public CaptureTheFlagEditor(JSONMap map){
        setDisplayString("Capture the Flag");
        if(map.getJSONCaptureTheFlag() == null)
            map.setJSONCaptureTheFlag(new JSONCaptureTheFlag());
        this.jsonCaptureTheFlag = map.getJSONCaptureTheFlag();

    }
    public void setBlueFlag(Player player){
        //TODO: Implement setBlueFlag
    }
    public void setRedFlag(Player player){
        //TODO: Implement setRedFlag
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
    public String getGamemode() {return Gamemodes.CAPTURETHEFLAG;}

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
