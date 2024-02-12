package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONRingBearer;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;

import java.util.ArrayList;

public class RingBearerEditor extends RedBlueSpawnListEditor {
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
}
