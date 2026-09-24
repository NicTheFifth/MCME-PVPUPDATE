package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamConquest;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.SpecialPointListEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


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
        player.sendMessage(mm.deserialize("<aqua>Capture point removed from Team Conquest.</aqua>"));
    }
    public void AddCapturePoint(Player player){
        JSONLocation JSONSpawn = new JSONLocation(player.getLocation());
        ((JSONTeamConquest)jsonGamemode).getCapturePoints().add(JSONSpawn);
        player.sendMessage(mm.deserialize("<aqua>Capture point added to Team Conquest.</aqua>"));
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
        JSONTeamConquest teamConquest = (JSONTeamConquest)jsonGamemode;
        player.sendMessage(mm.deserialize("""
                <aqua>Current selected gamemode: Team Conquest
                  Max players: <max>
                  Blue spawn set: <bs>
                  Red spawn set: <rs>
                  Capture points: <cp></aqua>""",
                Placeholder.parsed("max", String.valueOf(teamConquest.getMaximumPlayers())),
                Placeholder.parsed("bs", String.valueOf(teamConquest.getBlueSpawns().size())),
                Placeholder.parsed("rs", String.valueOf(teamConquest.getRedSpawns().size())),
                Placeholder.parsed("cp", String.valueOf(teamConquest.getCapturePoints().size()))));
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
