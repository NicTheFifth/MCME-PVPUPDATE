package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamSlayer;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONRedBlueSpawnListGamemode;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.RedBlueSpawnListEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;

import java.util.ArrayList;

public class TeamSlayerEditor extends RedBlueSpawnListEditor {
    private TeamSlayerEditor(){}
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

    @Override
    public String[] getInfo(){
        return new String[]{
                String.format(Style.INFO + "Current selected gamemode: Team Slayer."),
                String.format(Style.INFO + "Max players: %d",
                    jsonGamemode.getMaximumPlayers()),
                String.format(Style.INFO + "Blue spawns: %s",
                    ((JSONRedBlueSpawnListGamemode)jsonGamemode).getBlueSpawns().size()),
                String.format(Style.INFO + "Red spawns: %s",
                    ((JSONRedBlueSpawnListGamemode)jsonGamemode).getRedSpawns().size())
        };
    }
}
