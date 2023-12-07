package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONOneInTheQuiver;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class OneInTheQuiverEditor implements GamemodeEditor{
    JSONOneInTheQuiver jsonOneInTheQuiver;
    private OneInTheQuiverEditor(){}
    public OneInTheQuiverEditor(JSONMap map){
        this.jsonOneInTheQuiver = map.getJSONOneInTheQuiver();
        this.jsonOneInTheQuiver.setSpawns(new ArrayList<>());
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
    public String[] setMaxPlayers(Integer maxPlayers) {
        jsonOneInTheQuiver.setMaximumPlayers(maxPlayers);
        return new String[]{String.format(Style.INFO + "Set the max players to %d.", maxPlayers)};
    }
    @Override
    public String getGamemode(){return "One in the Quiver";}
    @Override
    public String[] getInfo(){
        return new String[]{
                String.format(Style.INFO + "Current selected gamemode: One in the Quiver."),
                String.format(Style.INFO + "Max players: %d", jsonOneInTheQuiver.getMaximumPlayers()),
                String.format(Style.INFO + "Spawn set: %s", jsonOneInTheQuiver.getSpawns().size())
        };
    }
}
