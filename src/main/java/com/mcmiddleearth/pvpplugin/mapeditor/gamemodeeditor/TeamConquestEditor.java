package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamConquest;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONRedBlueSpawnGamemode;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class TeamConquestEditor extends RedBlueSpawnEditor {
    private TeamConquestEditor(){}

    public TeamConquestEditor(JSONMap map){
        if(map.getJSONTeamConquest() == null)
            map.setJSONTeamConquest(new JSONTeamConquest());
        if(map.getJSONTeamConquest().getCapturePoints() == null)
            map.getJSONTeamConquest().setCapturePoints(new ArrayList<>());
        setDisplayString("Team Conquest");
        this.jsonGamemode = map.getJSONTeamConquest();
    }
    public void DeleteCapturePoint(int toDelete, Player player){
        ((JSONTeamConquest)jsonGamemode).getCapturePoints().remove(toDelete);
        sendBaseComponent(
            new ComponentBuilder("Capture point removed from Team Conquest.")
                .color(Style.INFO)
                .create(),
            player);
    }
    public void AddCapturePoint(Player player){
        JSONLocation JSONSpawn = new JSONLocation(player.getLocation());
        ((JSONTeamConquest)jsonGamemode).getCapturePoints().add(JSONSpawn);
        sendBaseComponent(new ComponentBuilder("Capture point added to Team Conquest.")
            .color(Style.INFO)
            .create(),
        player);
    }
    @Override
    public String getGamemode(){return Gamemodes.TEAMCONQUEST;}

    @Override
    public String[] getInfo(){
        return new String[]{
                String.format(Style.INFO + "Current selected gamemode: Team Conquest."),
                String.format(Style.INFO + "Max players: %d",
                    jsonGamemode.getMaximumPlayers()),
                String.format(Style.INFO + "Blue spawn set: %b",
                    ((JSONRedBlueSpawnGamemode)jsonGamemode).getBlueSpawn()),
                String.format(Style.INFO + "Red spawn set: %b",
                    ((JSONRedBlueSpawnGamemode)jsonGamemode).getRedSpawn()),
                String.format(Style.INFO + "Goal points: %s",
                    ((JSONTeamConquest)jsonGamemode).getCapturePoints().size())
        };
    }
    public Integer amountOfCapturePoints(){
        return ((JSONTeamConquest)jsonGamemode).getCapturePoints().size();
    }
}
