package com.mcmiddleearth.mcme.pvpplugin.util;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.transcribers.LocationTranscriber;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapEditor {

    public enum EditorState{
        MAP,
        CAPTURETHEFLAG,
        FREEFORALL,
        INFECTED,
        TEAMCONQUEST,
        TEAMDEATHMATCH,
        TEAMSLAYER,
        DEATHRUN
    }
    EditorState state;

    JSONMap map;

    public MapEditor(JSONMap map){
        this.map = map;
        state = EditorState.MAP;
    }
    public void setArea(PVPPlugin pvpPlugin, Player source) {
        BukkitPlayer bukkitP = new BukkitPlayer(source);
        LocalSession session = pvpPlugin.getWorldEditPlugin().getWorldEdit().getSessionManager().get(bukkitP);

        try{
            Region r = session.getSelection(new BukkitWorld(source.getWorld()));
            if(r.getHeight() < 250){
                source.sendMessage(Style.INFO + "I think you forgot to do //expand vert!");
            }
            else{
                List<BlockVector2> wePoints = r.polygonize(1000);
                ArrayList<JSONLocation> bPoints = new ArrayList<>();

                for(BlockVector2 point : wePoints){
                    bPoints.add(LocationTranscriber.TranscribeToJSON(new Location(source.getWorld(), point.getX(), 1, point.getZ())));
                }

                map.setRegionPoints(bPoints);
                source.sendMessage(Style.INFO + "Area set!");
            }
        }
        catch(IncompleteRegionException e){
            source.sendMessage(Style.ERROR + "You don't have a region selected!");
        }
    }

    public void setBlueSpawn(Location loc){
        JSONLocation location = LocationTranscriber.TranscribeToJSON(loc);
        switch(state){
            case CAPTURETHEFLAG:
                map.getJSONCaptureTheFlag().setBlueSpawn(location);
                break;
            case TEAMSLAYER:
                map.getJSONTeamSlayer().setBlueSpawn(location);
                break;
            case TEAMCONQUEST:
                map.getJSONTeamConquest().setBlueSpawn(location);
                break;
            case TEAMDEATHMATCH:
                map.getJSONTeamDeathMatch().setBlueSpawn(location);
                break;
            default:
                break;
        }
    }

    public void setRedSpawn(Location loc){
        JSONLocation location = LocationTranscriber.TranscribeToJSON(loc);
        switch(state){
            case CAPTURETHEFLAG:
                map.getJSONCaptureTheFlag().setRedSpawn(location);
                break;
            case TEAMSLAYER:
                map.getJSONTeamSlayer().setRedSpawn(location);
                break;
            case TEAMCONQUEST:
                map.getJSONTeamConquest().setRedSpawn(location);
                break;
            case TEAMDEATHMATCH:
                map.getJSONTeamDeathMatch().setRedSpawn(location);
                break;
            default:
                break;
        }
    }

    public void setMax(Integer max){
        switch(state) {
            case MAP:
                break;
            case CAPTURETHEFLAG:
                map.getJSONCaptureTheFlag().setMaximumPlayers(max);
                break;
            case FREEFORALL:
                map.getJSONFreeForAll().setMaximumPlayers(max);
                break;
            case INFECTED:
                map.getJSONInfected().setMaximumPlayers(max);
                break;
            case TEAMSLAYER:
                map.getJSONTeamSlayer().setMaximumPlayers(max);
                break;
            case TEAMCONQUEST:
                map.getJSONTeamConquest().setMaximumPlayers(max);
                break;
            case TEAMDEATHMATCH:
                map.getJSONTeamDeathMatch().setMaximumPlayers(max);
                break;
        }
    }
    public EditorState getState(){return state;}
    public void setState(EditorState state){
        this.state = state;
    }
}
