package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONFreeForAll;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.SpawnListEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;

public class FreeForAllEditor extends SpawnListEditor {
    private FreeForAllEditor(){}
    public FreeForAllEditor(JSONMap map){
        setDisplayString("Free for All");
        if(map.getJSONFreeForAll() == null)
            map.setJSONFreeForAll(new JSONFreeForAll());
        this.jsonGamemode = map.getJSONFreeForAll();
    }

    @Override
    public String getGamemode() {
        return Gamemodes.FREEFORALL;
    }

    @Override
    public String[] getInfo() {
        return new String[0];
    }
}
