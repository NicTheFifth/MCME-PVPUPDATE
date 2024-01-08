package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamConquest;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.GamemodeEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.RedBlueSpawnEditor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class TeamConquestEditor extends RedBlueSpawnEditor {
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
    public void setBlueSpawn(Player player){
        Location blueSpawn = player.getLocation();
        JSONLocation JSONBlueSpawn = new JSONLocation(blueSpawn);
        jsonTeamConquest.setBlueSpawn(JSONBlueSpawn);
        sendBaseComponent(
            new ComponentBuilder("Blue spawn set for Team Conquest.")
                .color(Style.INFO)
                .create(),
            player);
    }
    public void setRedSpawn(Player player) {
        Location redSpawn = player.getLocation();
        JSONLocation JSONRedSpawn = new JSONLocation(redSpawn);
        jsonTeamConquest.setRedSpawn(JSONRedSpawn);
        sendBaseComponent(
            new ComponentBuilder("Red spawn set for Team Conquest.")
                .color(Style.INFO)
                .create(),
            player);
    }
    public void DeleteCapturePoint(int toDelete, Player player){
        jsonTeamConquest.getCapturePoints().remove(toDelete);
        sendBaseComponent(
            new ComponentBuilder("Capture point removed from Team Conquest.")
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
