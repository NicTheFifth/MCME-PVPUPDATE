package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONRingBearer;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONRedBlueSpawnListGamemode;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.RedBlueSpawnListEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;

import java.util.ArrayList;

public class RingBearerEditor extends RedBlueSpawnListEditor {
    private RingBearerEditor(){}
    public RingBearerEditor(JSONMap map){
        if(map.getJSONRingBearer() == null)
            map.setJSONRingBearer(new JSONRingBearer());
        if(map.getJSONRingBearer().getBlueSpawns() == null)
            map.getJSONRingBearer().setBlueSpawns(new ArrayList<>());
        if(map.getJSONRingBearer().getRedSpawns() == null)
            map.getJSONRingBearer().setRedSpawns(new ArrayList<>());
        setDisplayString("Ringbearer");
        jsonGamemode = map.getJSONRingBearer();
    }
    @Override
    public String getGamemode() {return Gamemodes.RINGBEARER;}

    @Override
    public String[] getInfo(){
        return new String[]{
                String.format(Style.INFO + "Current selected gamemode: Ringbearer."),
                String.format(Style.INFO + "Max players: %d",
                    jsonGamemode.getMaximumPlayers()),
            String.format(Style.INFO + "Blue spawns: %s",
                ((JSONRedBlueSpawnListGamemode)jsonGamemode).getBlueSpawns().size()),
            String.format(Style.INFO + "Red spawns: %s",
                ((JSONRedBlueSpawnListGamemode)jsonGamemode).getRedSpawns().size())
        };
    }
}
