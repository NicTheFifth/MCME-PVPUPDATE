package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONOneInTheQuiver;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class OneInTheQuiverEditor implements GamemodeEditor{
    JSONOneInTheQuiver jsonOneInTheQuiver;
    private OneInTheQuiverEditor(){}
    public OneInTheQuiverEditor(JSONMap map){
        if(map.getJSONOneInTheQuiver() == null)
            map.setJSONOneInTheQuiver(new JSONOneInTheQuiver());
        if(map.getJSONOneInTheQuiver().getSpawns() == null)
            map.getJSONOneInTheQuiver().setSpawns(new ArrayList<>());
        this.jsonOneInTheQuiver = map.getJSONOneInTheQuiver();
    }
    public String[] DeleteSpawn(int toDelete){
        jsonOneInTheQuiver.getSpawns().remove(toDelete);
        return new String[]{Style.INFO + "Spawn removed from One in the Quiver."};
    }
    public String[] AddSpawn(Location spawn){
        JSONLocation JSONSpawn = new JSONLocation(spawn);
        jsonOneInTheQuiver.getSpawns().add(JSONSpawn);
        return new String[]{Style.INFO + "Spawn added to One in the Quiver."};
    }
    @Override
    public void setMaxPlayers(Integer maxPlayers, Player player) {
        jsonOneInTheQuiver.setMaximumPlayers(maxPlayers);
        sendBaseComponent(
            new ComponentBuilder(String.format("Set the max players to %d.",
                maxPlayers))
                .color(Style.INFO)
                .create(),
            player);
    }
    @Override
    public String getGamemode(){return "One in the Quiver";}

    @Override
    public void setMap(JSONMap map) {
        if(map.getJSONOneInTheQuiver() == null)
            map.setJSONOneInTheQuiver(new JSONOneInTheQuiver());
        if(map.getJSONOneInTheQuiver().getSpawns() == null)
            map.getJSONOneInTheQuiver().setSpawns(new ArrayList<>());
        this.jsonOneInTheQuiver = map.getJSONOneInTheQuiver();
    }

    @Override
    public String[] getInfo(){
        return new String[]{
                String.format(Style.INFO + "Current selected gamemode: One in the Quiver."),
                String.format(Style.INFO + "Max players: %d", jsonOneInTheQuiver.getMaximumPlayers()),
                String.format(Style.INFO + "Spawn set: %s", jsonOneInTheQuiver.getSpawns().size())
        };
    }
}
