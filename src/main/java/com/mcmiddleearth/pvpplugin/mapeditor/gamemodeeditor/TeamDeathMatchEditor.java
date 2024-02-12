package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamDeathMatch;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;

public class TeamDeathMatchEditor extends RedBlueSpawnEditor {
    public TeamDeathMatchEditor(JSONMap map){
        if(map.getJSONTeamDeathMatch() == null)
            map.setJSONTeamDeathMatch(new JSONTeamDeathMatch());
        setDisplayString("Team Deathmatch");
        this.jsonGamemode = map.getJSONTeamDeathMatch();
    }
    @Override
    public String getGamemode(){return Gamemodes.TEAMDEATHMATCH;}
}