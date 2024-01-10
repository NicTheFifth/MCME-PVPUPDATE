package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamDeathMatch;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONRedBlueSpawnGamemode;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.RedBlueSpawnEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;

public class TeamDeathMatchEditor extends RedBlueSpawnEditor {
    private TeamDeathMatchEditor(){}
    public TeamDeathMatchEditor(JSONMap map){
        if(map.getJSONTeamDeathMatch() == null)
            map.setJSONTeamDeathMatch(new JSONTeamDeathMatch());
        setDisplayString("Team Deathmatch");
        this.jsonGamemode = map.getJSONTeamDeathMatch();
    }
    @Override
    public String getGamemode(){return Gamemodes.TEAMDEATHMATCH;}

    @Override
    public String[] getInfo(){
        return new String[]{
                String.format(Style.INFO + "Current selected gamemode: Team Deathmatch."),
                String.format(Style.INFO + "Max players: %d",
                    jsonGamemode.getMaximumPlayers()),
                String.format(Style.INFO + "Blue spawn set: %b",
                    ((JSONRedBlueSpawnGamemode)jsonGamemode).getBlueSpawn()),
                String.format(Style.INFO + "Red spawn set: %b",
                    ((JSONRedBlueSpawnGamemode)jsonGamemode).getRedSpawn())
        };
    }
}