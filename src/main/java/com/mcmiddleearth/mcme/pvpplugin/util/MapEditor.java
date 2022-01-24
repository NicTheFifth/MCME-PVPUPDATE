package com.mcmiddleearth.mcme.pvpplugin.util;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.transcribers.LocationTranscriber;
import org.bukkit.Location;

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
