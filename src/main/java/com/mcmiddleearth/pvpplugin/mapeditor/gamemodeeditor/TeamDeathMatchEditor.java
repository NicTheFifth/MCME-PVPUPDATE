package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamDeathMatch;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class TeamDeathMatchEditor implements GamemodeEditor, RedBlueTeamEditor {
    JSONTeamDeathMatch jsonTeamDeathMatch;
    private TeamDeathMatchEditor(){}
    public TeamDeathMatchEditor(JSONMap map){
        if(map.getJSONTeamDeathMatch() == null)
            map.setJSONTeamDeathMatch(new JSONTeamDeathMatch());
        this.jsonTeamDeathMatch = map.getJSONTeamDeathMatch();
    }
    @Override
    public void setMaxPlayers(Integer maxPlayers, Player player) {
        jsonTeamDeathMatch.setMaximumPlayers(maxPlayers);
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
        jsonTeamDeathMatch.setBlueSpawn(JSONBlueSpawn);
        sendBaseComponent(
            new ComponentBuilder("Blue spawn set for Team Death Match.")
                .color(Style.INFO)
                .create(),
            player);
    }
    public void setRedSpawn(Player player){
        Location redSpawn = player.getLocation();
        JSONLocation JSONRedSpawn = new JSONLocation(redSpawn);
        jsonTeamDeathMatch.setRedSpawn(JSONRedSpawn);
        sendBaseComponent(
            new ComponentBuilder("Blue spawn set for Team Death Match.")
                .color(Style.INFO)
                .create(),
            player);
    }
    @Override
    public String getGamemode(){return "Team Deathmatch";}

    @Override
    public void setMap(JSONMap map) {
        if(map.getJSONTeamDeathMatch() == null)
            map.setJSONTeamDeathMatch(new JSONTeamDeathMatch());
        this.jsonTeamDeathMatch = map.getJSONTeamDeathMatch();
    }

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