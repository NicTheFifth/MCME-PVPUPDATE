package com.mcmiddleearth.pvpplugin.mapeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.*;
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
import java.util.Objects;

public class MapEditor {

    JSONMap map;
    // List<JSONLocation> regionPoints;
    // String title;
    // JSONLocation spawn;
    // String resourcePack;

    GamemodeEditor gamemodeEditor;

    public MapEditor(JSONMap map){
        this.map = map;
        gamemodeEditor = null;
    }

    //<editor-fold desc="Basic map edits">
    public String[] setArea(Player source) {
        BukkitPlayer bukkitP = new BukkitPlayer(source);
        LocalSession session = PVPPlugin.getInstance()
                .getWorldEditPlugin().getWorldEdit()
                .getSessionManager().get(bukkitP);

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
    public String[] setSpawn(Location location) {
        map.setSpawn(new JSONLocation(location));
        return new String[]{Style.INFO + String.format("Spawn location set for %s", map.getTitle())};
    }
    public String[] setRP(String rpName) {
        map.setResourcePack(rpName);
        return new String[]{Style.INFO + String.format("%s has been set as the resource pack on %s", rpName, map.getTitle())};
    }
    //</editor-fold>

    public List<String> getStatus(){
        return List.of(
                String.format(Style.INFO + "Map name: %s", map.getTitle()),
                String.format(Style.INFO + "Resource pack: %s", map.getResourcePack()),
                String.format(Style.INFO + "Gamemodes:"),
                String.format(Style.INFO + "\t Capture The Flag: %b", Objects.isNull(map.getJSONCaptureTheFlag())),
                String.format(Style.INFO + "\t Death Run: %b", Objects.isNull(map.getJSONDeathRun())),
                String.format(Style.INFO + "\t Free for All: %b", Objects.isNull(map.getJSONFreeForAll())),
                String.format(Style.INFO + "\t Infected: %b", Objects.isNull(map.getJSONInfected())),
                String.format(Style.INFO + "\t One in the Quiver: %b", Objects.isNull(map.getJSONOneInTheQuiver())),
                String.format(Style.INFO + "\t Ringbearer: %b", Objects.isNull(map.getJSONRingBearer())),
                String.format(Style.INFO + "\t Team Conquest: %b", Objects.isNull(map.getJSONTeamConquest())),
                String.format(Style.INFO + "\t Team Deathmatch: %b", Objects.isNull(map.getJSONTeamDeathMatch())),
                String.format(Style.INFO + "\t Team Slayer: %b", Objects.isNull(map.getJSONTeamSlayer()))
        );
    }
    public String[] getShortStatus(){
        return new String[]{
                String.format(Style.INFO + "Map name: %s", map.getTitle()),
                String.format(Style.INFO + "Resource pack: %s", map.getResourcePack()),
                String.format(Style.INFO + "Selected Gamemode: %s", (gamemodeEditor == null ? "None": gamemodeEditor.getGamemode()))};
    }
    public JSONMap getMap() {
        return map;
    }
    public void setMap(String mapName){
        map = PVPPlugin.getInstance().getMaps().get(mapName);
    }

    public GamemodeEditor getGamemodeEditor(){
        return gamemodeEditor;
    }

    public String[] setGamemodeEditor(String gamemode){
        switch(gamemode){
            case "capturetheflag":
                gamemodeEditor = new CaptureTheFlagEditor(map);
            case "deathrun":
                gamemodeEditor = new DeathRunEditor(map);
            case "infected":
                gamemodeEditor = new InfectedEditor(map);
            case "oneinthequiver":
                gamemodeEditor = new OneInTheQuiverEditor(map);
            case "ringbearer":
                gamemodeEditor = new RingBearerEditor(map);
            case "teamconquest":
                gamemodeEditor = new TeamConquestEditor(map);
            case "teamdeathmatch":
                gamemodeEditor = new TeamDeathMatchEditor(map);
            case "teamslayer":
                gamemodeEditor = new TeamSlayerEditor(map);
        }
        return new String[]{String.format("Set the gamemode to %s. Current state of the gamemode is:", gamemode)};
    }
}
