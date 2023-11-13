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
    public List<String> setMaxPlayers(Integer maxPlayers) {
        jsonTeamDeathMatch.setMaximumPlayers(maxPlayers);
        return List.of(String.format(Style.INFO + "Set the max players to %d.", maxPlayers));
    }

    public List<String> setBlueSpawn(Location blueSpawn, JSONMap map){
        JSONLocation JSONBlueSpawn = new JSONLocation(blueSpawn);
        map.getJSONTeamDeathMatch().setBlueSpawn(JSONBlueSpawn);
        return List.of(Style.INFO + String.format("Blue spawn set for Team Deathmatch on %s", map.getTitle()));
    }

    public List<String> setRedSpawn(Location redSpawn, JSONMap map){
        JSONLocation JSONRedSpawn = new JSONLocation(redSpawn);
        map.getJSONTeamDeathMatch().setRedSpawn(JSONRedSpawn);
        return List.of(Style.INFO + String.format("Red spawn set for Team Deathmatch on %s", map.getTitle()));
    }

    @Override
    public String getGamemode(){return "Team Deathmatch";}
}
