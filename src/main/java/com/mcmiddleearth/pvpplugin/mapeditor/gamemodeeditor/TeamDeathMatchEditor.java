package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamDeathMatch;
import org.bukkit.Location;

import java.util.List;

public class TeamDeathMatchEditor implements GamemodeEditor{

    JSONTeamDeathMatch jsonTeamDeathMatch;

    private TeamDeathMatchEditor(){}

    public TeamDeathMatchEditor(JSONMap jsonMap){
        this.jsonTeamDeathMatch = jsonMap.getJSONTeamDeathMatch();
    }
    @Override
    public String[] setMaxPlayers(Integer maxPlayers) {
        jsonTeamDeathMatch.setMaximumPlayers(maxPlayers);
        return new String[]{String.format(Style.INFO + "Set the max players to %d.", maxPlayers)};
    }
    public List<String> setBlueSpawn(Location blueSpawn){
        JSONLocation JSONBlueSpawn = new JSONLocation(blueSpawn);
        jsonTeamDeathMatch.setBlueSpawn(JSONBlueSpawn);
        return List.of(Style.INFO + "Blue spawn set for Team Deathmatch.");
    }
    public List<String> setRedSpawn(Location redSpawn){
        JSONLocation JSONRedSpawn = new JSONLocation(redSpawn);
        jsonTeamDeathMatch.setRedSpawn(JSONRedSpawn);
        return List.of(Style.INFO + "Red spawn set for Team Deathmatch.");
    }
    @Override
    public String getGamemode(){return "Team Deathmatch";}
}
