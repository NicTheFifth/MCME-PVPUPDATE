package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamSlayer;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;

import java.util.ArrayList;

public class TeamSlayerEditor extends RedBlueSpawnListEditor {
    public TeamSlayerEditor(JSONMap map){
        if(map.getJSONTeamSlayer() == null)
            map.setJSONTeamSlayer(new JSONTeamSlayer());
        if(map.getJSONTeamSlayer().getBlueSpawns() == null)
            map.getJSONTeamSlayer().setBlueSpawns(new ArrayList<>());
        if(map.getJSONTeamSlayer().getRedSpawns() == null)
            map.getJSONTeamSlayer().setRedSpawns(new ArrayList<>());
        setDisplayString("Team Slayer");
        this.jsonGamemode = map.getJSONTeamSlayer();
    }
    @Override
    public String getGamemode(){return Gamemodes.TEAMSLAYER;}
}
