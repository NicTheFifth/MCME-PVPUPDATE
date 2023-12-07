package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamConquest;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class TeamConquestEditor implements GamemodeEditor{
    JSONTeamConquest jsonTeamConquest;
    private TeamConquestEditor(){}

    public TeamConquestEditor(JSONMap jsonMap){
        this.jsonTeamConquest = jsonMap.getJSONTeamConquest();
        this.jsonTeamConquest.setCapturePoints(new ArrayList<>());
    }
    @Override
    public String[] setMaxPlayers(Integer maxPlayers) {
        jsonTeamConquest.setMaximumPlayers(maxPlayers);
        return new String[]{String.format(Style.INFO + "Set the max players to %d.", maxPlayers)};
    }
    public String[] setBlueSpawn(Location blueSpawn){
        JSONLocation JSONBlueSpawn = new JSONLocation(blueSpawn);
        jsonTeamConquest.setBlueSpawn(JSONBlueSpawn);
        return new String[]{Style.INFO + "Blue spawn set for Team Conquest."};
    }
    public String[] setRedSpawn(Location redSpawn) {
        JSONLocation JSONRedSpawn = new JSONLocation(redSpawn);
        jsonTeamConquest.setRedSpawn(JSONRedSpawn);
        return new String[]{Style.INFO + "Red spawn set for Team Conquest."};
    }
    public String[] DeleteGoal(int toDelete){
        jsonTeamConquest.getCapturePoints().remove(toDelete);
        return new String[]{Style.INFO + "Goal removed from Team Conquest."};
    }
    public String[] AddGoal(Location spawn){
        JSONLocation JSONSpawn = new JSONLocation(spawn);
        jsonTeamConquest.getCapturePoints().add(JSONSpawn);
        return new String[]{Style.INFO + "Goal added to Team Conquest."};
    }
    @Override
    public String getGamemode(){return "Team Conquest";}
    @Override
    public String[] getInfo(){
        return new String[]{
                String.format(Style.INFO + "Current selected gamemode: Team Conquest."),
                String.format(Style.INFO + "Max players: %d", jsonTeamConquest.getMaximumPlayers()),
                String.format(Style.INFO + "Blue spawn set: %b", jsonTeamConquest.getBlueSpawn()),
                String.format(Style.INFO + "Red spawn set: %b", jsonTeamConquest.getRedSpawn()),
                String.format(Style.INFO + "Goal points: %s", jsonTeamConquest.getCapturePoints().size())
        };
    }
}
