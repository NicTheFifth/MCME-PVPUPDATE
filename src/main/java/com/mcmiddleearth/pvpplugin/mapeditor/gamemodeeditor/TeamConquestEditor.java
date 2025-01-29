package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamConquest;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONRedBlueSpawnGamemode;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.SpecialPointListEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class TeamConquestEditor extends RedBlueSpawnListEditor implements SpecialPointListEditor {
    Map<String, SpecialPointListEditor.AddRemoveIndexTeleportQuartet> specialPointListNames =
        new HashMap<>();

    public TeamConquestEditor(JSONMap map){
        if(map.getJSONTeamConquest() == null) {
            map.setJSONTeamConquest(new JSONTeamConquest());
            map.getJSONTeamConquest().setBlueSpawns(new ArrayList<>());
            map.getJSONTeamConquest().setRedSpawns(new ArrayList<>());
        }
        if(map.getJSONTeamConquest().getCapturePoints() == null)
            map.getJSONTeamConquest().setCapturePoints(new ArrayList<>());
        setDisplayString("Team Conquest");
        this.jsonGamemode = map.getJSONTeamConquest();
        initSpecialPointListNames();
    }
    public void DeleteCapturePoint(Player player, int toDelete){
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
    public void TeleportToPoint(Player player, Integer index){

    }
    @Override
    public String getGamemode(){return Gamemodes.TEAMCONQUEST;}
    @Override
    public void ShowPoints(Player player) {
        AtomicInteger index = new AtomicInteger(0);
        List<JSONLocation> capturePoints =
            ((JSONTeamConquest)jsonGamemode).getCapturePoints();
        capturePoints.forEach(point ->
            MapEditor.SpawnMarker(point, String.format("capturePoint %d",
            index.getAndIncrement())));
    }
    @Override
    public void sendStatus(Player player){
        sendBaseComponent(
            new ComponentBuilder("Current selected gamemode: Team Conquest.")
                .color(Style.INFO)
                .append(String.format("Max players: %d",
                    jsonGamemode.getMaximumPlayers()))
                .color(Style.INFO)
                .append(String.format("Blue spawn set: %b",
                    ((JSONRedBlueSpawnGamemode)jsonGamemode).getBlueSpawn()))
                .color(Style.INFO)
                .append(String.format("Red spawn set: %b",
                    ((JSONRedBlueSpawnGamemode)jsonGamemode).getRedSpawn()))
                .color(Style.INFO)
                .append(String.format("Goal points: %s",
                    ((JSONTeamConquest)jsonGamemode).getCapturePoints().size()))
                .color(Style.INFO)
                .create(),
            player
        );
    }

    @Override
    public void initSpecialPointListNames() {
        getSpecialPointListNames().put("capturePoint",
            new SpecialPointListEditor.AddRemoveIndexTeleportQuartet(
                this::AddCapturePoint,
                this::DeleteCapturePoint,
                ((JSONTeamConquest)jsonGamemode).getCapturePoints()::size,
                this::TeleportToPoint
            ));
    }
    public Map<String, SpecialPointListEditor.AddRemoveIndexTeleportQuartet> getSpecialPointListNames(){
        return specialPointListNames;
    }
}
