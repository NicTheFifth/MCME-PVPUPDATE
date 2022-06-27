package com.mcmiddleearth.mcme.pvpplugin.util;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapEditor {

    public enum EditorState{
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
        state = null;
    }

    public String[] setArea(Player source) {
        BukkitPlayer bukkitP = new BukkitPlayer(source);
        LocalSession session = PVPPlugin.getInstance().getWorldEditPlugin().getWorldEdit().getSessionManager().get(bukkitP);

        try{
            Region r = session.getSelection(new BukkitWorld(source.getWorld()));
            if(r.getHeight() < 250){
                return new String[]{Style.INFO + "Use //expand vert then try again!"};
            }
            else{
                List<BlockVector2> wePoints = r.polygonize(1000);
                ArrayList<JSONLocation> bPoints = new ArrayList<>();

                for(BlockVector2 point : wePoints){
                    bPoints.add(new JSONLocation(new Location(source.getWorld(), point.getX(), 1, point.getZ())));
                }

                map.setRegionPoints(bPoints);
                return new String[]{Style.INFO + String.format("Area set for %s!", map.getTitle())};
            }
        }
        catch(IncompleteRegionException e){
            return new String[]{Style.ERROR + "You don't have a region selected!"};
        }
    }

    public String[] setTitle(String newName) {
        String oldName = map.getTitle();
        PVPPlugin.getInstance().getMaps().remove(oldName);
        File f = new File(PVPPlugin.getInstance().getMapDirectory() + System.getProperty("file.separator") + oldName);
        f.delete();
        map.setTitle(newName);
        PVPPlugin.getInstance().getMaps().put(newName, map);
        return new String[]{Style.INFO + String.format("%s has been renamed to %s.", oldName, newName)};
    }

    public String[] setRP(String rpName) {
        map.setResourcePack(rpName);
        return new String[]{Style.INFO + String.format("%s has been set as the resource pack on %s", rpName, map.getTitle())};
    }

    public String[] setGoal(Player source) {
        JSONLocation point = new JSONLocation(source.getLocation().add(0,1,0));
        if(state == EditorState.DEATHRUN) {
            map.getJSONDeathRun().setGoal(point);
            return new String[]{Style.INFO + String.format("Goal for deathrun has been set on %s", map.getTitle())};
        }
        return new String[]{Style.ERROR + "Please set the state to deathrun using /mapedit gamemode deathrun"};
    }

    public String[] createCapturePoint(Player source) {
        JSONLocation point = new JSONLocation(source.getLocation().add(0,1,0));
        if(state == EditorState.TEAMCONQUEST) {
            map.getJSONTeamConquest().getCapturePoints().add(point);
            return new String[]{Style.INFO + String.format("Capture point added for team conquest on %s", map.getTitle())};
        }
        return new String[]{Style.ERROR + "Please set the state to deathrun using /mapedit gamemode teamconquest"};
    }

    public String[] delCapturePoint(int point) {
        List<JSONLocation> capturePoints = map.getJSONTeamConquest().getCapturePoints();
        if(state == EditorState.TEAMCONQUEST && point < capturePoints.size() ){
            capturePoints.remove(point);
            return new String[]{Style.INFO + String.format("Capture point removed for team conquest on %s", map.getTitle())};
        }
        return (state != EditorState.TEAMCONQUEST) ?
            (new String[]{Style.ERROR + "Please set the state to deathrun using /mapedit gamemode teamconquest"}):
            (new String[]{Style.ERROR + String.format("Please use a number under %d, as that is the amount of points.", capturePoints.size())});
    }

    public String[] setGamemode(String gamemode) {
        switch(gamemode){
            case "capturetheflag":
                setState(EditorState.CAPTURETHEFLAG);
                break;
            case "freeforall":
                setState(EditorState.FREEFORALL);
                break;
            case "infected":
                setState(EditorState.INFECTED);
                break;
            case "teamconquest":
                setState(EditorState.TEAMCONQUEST);
                break;
            case "teamdeathmatch":
                setState(EditorState.TEAMDEATHMATCH);
                break;
            case "teamslayer":
                setState(EditorState.TEAMSLAYER);
                break;
            case "deathrun":
                setState(EditorState.DEATHRUN);
                break;
            default:
                return new String[]{Style.ERROR + String.format("%s is not available as a gamemode, please create a report in discord, the dev public channel.", gamemode)};
        }
        return new String[]{Style.INFO + String.format("Gamemode to edit is set to %s on map %s", gamemode, map.getTitle())};
    }

    public String[] setBlueSpawn(Location loc){
        JSONLocation location = new JSONLocation(loc);
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
                return new String[]{Style.INFO + "Please set the state to either capture the flag, team slayer, team conquest or team death match, using /mapedit gamemode <gamemode>"};
        }
        return new String[]{Style.INFO + String.format("Blue spawn set for %s on %s", state.toString(), map.getTitle())};
    }

    public String[] setRedSpawn(Location loc){
        JSONLocation location = new JSONLocation(loc);
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
                return new String[]{Style.INFO + "Please set the state to either capture the flag, team slayer, team conquest or team death match, using /mapedit gamemode <gamemode>"};
        }
        return new String[]{Style.INFO + String.format("Red spawn set for %s on %s", state.toString(), map.getTitle())};
    }

    public String[] setDeathSpawn(Location loc) {
        map.getJSONDeathRun().setDeathSpawn(new JSONLocation(loc));
        return new String[]{Style.INFO + String.format("Death spawn set for deathrun on %s", map.getTitle())};
    }

    public String[] setRunnerSpawn(Location loc) {
        map.getJSONDeathRun().setRunnerSpawn(new JSONLocation(loc));
        return new String[]{Style.INFO + String.format("Runner spawn set for deathrun on %s", map.getTitle())};
    }

    public String[] setInfectedSpawn(Location loc) {
        map.getJSONInfected().setInfectedSpawn(new JSONLocation(loc));
        return new String[]{Style.INFO + String.format("Infected spawn set for infected on %s", map.getTitle())};
    }

    public String[] setSurvivorSpawn(Location loc) {
        map.getJSONInfected().setSurvivorSpawn(new JSONLocation(loc));
        return new String[]{Style.INFO + String.format("Survivor spawn set for infected on %s", map.getTitle())};
    }

    public String[] setMax(Integer max){
        switch(state) {
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
            default:
                return new String[]{Style.ERROR + "Please set the state to a gamemode using /mapedit gamemode <gamemode>"};
        }
        return new String[]{Style.INFO + String.format("Set %d for %s on %s", max, state.toString(), map.getTitle())};
    }

    public EditorState getState(){return state;}
    public void setState(EditorState state){
        this.state = state;
    }
}
