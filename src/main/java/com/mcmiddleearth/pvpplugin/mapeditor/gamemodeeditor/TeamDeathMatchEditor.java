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
        jsonTeamDeathMatch.setMaximumPlayers(maxPlayers);
        return new String[]{String.format(Style.INFO + "Set the max players to %d.", maxPlayers)};
    }
    public String[] setBlueSpawn(Location blueSpawn){
        JSONLocation JSONBlueSpawn = new JSONLocation(blueSpawn);
        jsonTeamDeathMatch.setBlueSpawn(JSONBlueSpawn);
        return new String[]{Style.INFO + "Blue spawn set for Team Deathmatch."};
    }
    public String[] setRedSpawn(Location redSpawn){
        JSONLocation JSONRedSpawn = new JSONLocation(redSpawn);
        jsonTeamDeathMatch.setRedSpawn(JSONRedSpawn);
        return new String[]{Style.INFO + "Red spawn set for Team Deathmatch."};
    }
    @Override
    public String getGamemode(){return "Team Deathmatch";}
    @Override
    public String[] getInfo(){
        return new String[]{
                String.format(Style.INFO + "Current selected gamemode: Team Deathmatch."),
                String.format(Style.INFO + "Max players: %d", jsonTeamDeathMatch.getMaximumPlayers()),
                String.format(Style.INFO + "Blue spawn set: %b", jsonTeamDeathMatch.getBlueSpawn()),
                String.format(Style.INFO + "Red spawn set: %b", jsonTeamDeathMatch.getRedSpawn())
        };
    }
}