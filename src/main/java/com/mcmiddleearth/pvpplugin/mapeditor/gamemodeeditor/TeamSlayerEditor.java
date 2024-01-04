package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamSlayer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class TeamSlayerEditor implements GamemodeEditor{
    JSONTeamSlayer jsonTeamSlayer;
    private TeamSlayerEditor(){}
    public TeamSlayerEditor(JSONMap map){
        if(map.getJSONTeamSlayer() == null)
            map.setJSONTeamSlayer(new JSONTeamSlayer());
        this.jsonTeamSlayer = map.getJSONTeamSlayer();
    }
    @Override
    public void setMaxPlayers(Integer maxPlayers, Player player) {
        jsonTeamSlayer.setMaximumPlayers(maxPlayers);
        sendBaseComponent(
            new ComponentBuilder(String.format("Set the max players to %d.",
                maxPlayers))
                .color(Style.INFO)
                .create(),
            player);
    }
    public String[] DeleteBlueSpawn(int toDelete){
        jsonTeamSlayer.getBlueSpawn().remove(toDelete);
        return new String[]{Style.INFO + "Spawn removed from One in the Quiver."};
    }
    public String[] AddBlueSpawn(Location blueSpawn){
        JSONLocation JSONBlueSpawn = new JSONLocation(blueSpawn);
        jsonTeamSlayer.getBlueSpawn().add(JSONBlueSpawn);
        return new String[]{Style.INFO + "Blue spawn added to Team Slayer."};
    }
    public String[] DeleteRedSpawn(int toDelete){
        jsonTeamSlayer.getRedSpawn().remove(toDelete);
        return new String[]{Style.INFO + "Spawn removed from One in the Quiver."};
    }
    public String[] addRedSpawn(Location redSpawn){
        JSONLocation JSONRedSpawn = new JSONLocation(redSpawn);
        jsonTeamSlayer.getRedSpawn().add(JSONRedSpawn);
        return new String[]{Style.INFO + "Red spawn added to Team Slayer."};
    }
    @Override
    public String getGamemode(){return "Team Slayer";}

    @Override
    public void setMap(JSONMap map) {
        if(map.getJSONTeamSlayer() == null)
            map.setJSONTeamSlayer(new JSONTeamSlayer());
        this.jsonTeamSlayer = map.getJSONTeamSlayer();
    }

    @Override
    public String[] getInfo(){
        return new String[]{
                String.format(Style.INFO + "Current selected gamemode: Team Slayer."),
                String.format(Style.INFO + "Max players: %d", jsonTeamSlayer.getMaximumPlayers()),
                String.format(Style.INFO + "Blue spawns: %s", jsonTeamSlayer.getBlueSpawn().size()),
                String.format(Style.INFO + "Red spawns: %s", jsonTeamSlayer.getRedSpawn().size())
        };
    }
}
