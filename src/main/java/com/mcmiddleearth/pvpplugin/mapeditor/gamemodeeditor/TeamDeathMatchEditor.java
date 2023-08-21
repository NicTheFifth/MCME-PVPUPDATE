package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamDeathMatch;
import org.bukkit.Location;

public class TeamDeathMatchEditor implements GamemodeEditor{

    JSONTeamDeathMatch jsonTeamDeathMatch;

    private TeamDeathMatchEditor(){}

    public TeamDeathMatchEditor(JSONMap jsonMap){
        this.jsonTeamDeathMatch = jsonMap.getJSONTeamDeathMatch();
    }
    @Override
    public String[] setMaxPlayers(Integer maxPlayers) {
        return new String[0];
    }

    public String[] setBlueSpawn(Location blueSpawn, JSONMap map){
        JSONLocation JSONBlueSpawn = new JSONLocation(blueSpawn);
        map.getJSONTeamDeathMatch().setBlueSpawn(JSONBlueSpawn);
        return new String[]{Style.INFO + String.format("Blue spawn set for Team Deathmatch on %s", map.getTitle())};
    }
}
