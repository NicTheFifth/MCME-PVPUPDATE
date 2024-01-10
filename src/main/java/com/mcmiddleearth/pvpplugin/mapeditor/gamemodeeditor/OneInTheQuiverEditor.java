package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONOneInTheQuiver;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONSpawnListGamemode;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.SpawnListEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;

import java.util.ArrayList;

public class OneInTheQuiverEditor extends SpawnListEditor {
    private OneInTheQuiverEditor(){}
    public OneInTheQuiverEditor(JSONMap map){
        if(map.getJSONOneInTheQuiver() == null)
            map.setJSONOneInTheQuiver(new JSONOneInTheQuiver());
        if(map.getJSONOneInTheQuiver().getSpawns() == null)
            map.getJSONOneInTheQuiver().setSpawns(new ArrayList<>());
        setDisplayString("One in the Quiver");
        this.jsonGamemode = map.getJSONOneInTheQuiver();
    }
    @Override
    public String getGamemode(){return Gamemodes.ONEINTHEQUIVER;}

    @Override
    public String[] getInfo(){
        return new String[]{
                String.format(Style.INFO + "Current selected gamemode: One in the Quiver."),
                String.format(Style.INFO + "Max players: %d",
                    jsonGamemode.getMaximumPlayers()),
                String.format(Style.INFO + "Spawn set: %s",
                    ((JSONSpawnListGamemode)jsonGamemode).getSpawns().size())
        };
    }
}
