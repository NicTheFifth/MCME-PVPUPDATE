package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamConquest;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class TeamConquestEditor implements GamemodeEditor{
    JSONTeamConquest jsonTeamConquest;
    private TeamConquestEditor(){}

    public TeamConquestEditor(JSONMap map){
        if(map.getJSONTeamConquest() == null)
            map.setJSONTeamConquest(new JSONTeamConquest());
        if(map.getJSONTeamConquest().getCapturePoints() == null)
            map.getJSONTeamConquest().setCapturePoints(new ArrayList<>());
        this.jsonTeamConquest = map.getJSONTeamConquest();
    }
    @Override
    public void setMaxPlayers(Integer maxPlayers, Player player) {
        jsonTeamConquest.setMaximumPlayers(maxPlayers);
        sendBaseComponent(
            new ComponentBuilder(String.format("Set the max players to %d.",
                maxPlayers))
                .color(Style.INFO)
                .create(),
            player);
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
    public void DeleteGoal(int toDelete, Player player){
        jsonTeamConquest.getCapturePoints().remove(toDelete);
        sendBaseComponent(
            new ComponentBuilder("Goal removed from Team Conquest.")
                .color(Style.INFO)
                .create(),
            player);
    }
    public void AddCapturePoint(Player player){
        JSONLocation JSONSpawn = new JSONLocation(player.getLocation());
        jsonTeamConquest.getCapturePoints().add(JSONSpawn);
        sendBaseComponent(new ComponentBuilder("Capture point added to Team Conquest.")
            .color(Style.INFO)
            .create(),
        player);
    }
    @Override
    public String getGamemode(){return "Team Conquest";}

    @Override
    public void setMap(JSONMap map) {
        if(map.getJSONTeamConquest() == null)
            map.setJSONTeamConquest(new JSONTeamConquest());
        if(map.getJSONTeamConquest().getCapturePoints() == null)
            map.getJSONTeamConquest().setCapturePoints(new ArrayList<>());
        this.jsonTeamConquest = map.getJSONTeamConquest();
    }

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

    public Integer amountOfCapturePoints(){
        return this.jsonTeamConquest.getCapturePoints().size();
    }
}
