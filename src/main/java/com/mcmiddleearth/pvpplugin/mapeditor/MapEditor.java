package com.mcmiddleearth.pvpplugin.mapeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.*;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.GamemodeEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.Region;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class MapEditor {

    JSONMap map;
    GamemodeEditor gamemodeEditor;

    public MapEditor(JSONMap map, Player player){
        this.map = map;
        sendBaseComponent(
            new ComponentBuilder(String.format("Map editor for %s created.",
                    map.getTitle()))
                .color(Style.INFO)
                .create(),
            player
        );
        sendStatus(player);
    }

    //<editor-fold desc="Basic map edits">
    public void setArea(Player source) {
        BukkitPlayer bukkitP = new BukkitPlayer(source);
        LocalSession session = PVPPlugin.getInstance()
                .getWorldEditPlugin().getWorldEdit()
                .getSessionManager().get(bukkitP);

        try{
            Region r = session.getSelection(new BukkitWorld(source.getWorld()));
            BaseComponent[] message;
            if(r.getHeight() < 250){
                message =
                    new ComponentBuilder("Use //expand vert then try again!")
                        .color(Style.ERROR)
                        .create();
            }
            else{
                List<BlockVector2> wePoints = r.polygonize(1000);
                ArrayList<JSONLocation> bPoints = new ArrayList<>();

                for(BlockVector2 point : wePoints){
                    bPoints.add(new JSONLocation(new Location(source.getWorld(), point.getX(), 1, point.getZ())));
                }

                map.setRegionPoints(bPoints);
                message =  new ComponentBuilder(String.format("Area set for " +
                    "%s!", map.getTitle()))
                        .color(Style.INFO)
                        .create();
            }
            sendBaseComponent(message, source);
        }
        catch(IncompleteRegionException e){
            sendBaseComponent(
                new ComponentBuilder("You don't have a region selected!")
                    .color(Style.ERROR)
                    .create(),
                source);
        }
    }
    public void setTitle(String newName, Player player) {
        String oldName = map.getTitle();
        PVPPlugin.getInstance().getMaps().remove(oldName);
        File f = new File(PVPPlugin.getInstance().getMapDirectory() +
                FileSystems.getDefault().getSeparator() +
                oldName);
        map.setTitle(newName);
        PVPPlugin.getInstance().getMaps().put(newName, map);
        try {
            f.delete();
            sendBaseComponent(
                new ComponentBuilder(String.format("%s has been renamed to %s.",
                    oldName, newName))
                    .color(Style.INFO)
                    .create(),
                player);
        }
        catch(SecurityException e){
            sendBaseComponent(
                new ComponentBuilder(String.format("%s couldn't be deleted.",
                    oldName))
                    .color(Style.ERROR)
                    .create(),
                player);
            Logger.getLogger("PVPPlugin").log(Level.WARNING,
                String.format("%s couldn't be deleted.", oldName));
        }
    }
    public void setSpawn(Player player) {
        Location location = player.getLocation();
        map.setSpawn(new JSONLocation(location));
        BaseComponent[] message = new ComponentBuilder(String.format("Spawn " +
                "location set for" +
                " %s",
            map.getTitle()))
            .color(Style.INFO)
            .create();
        sendBaseComponent(message, player);
    }
    public void setRP(String rpName, Player player) {
        map.setResourcePack(rpName);
        sendBaseComponent(
            new ComponentBuilder(String.format("%s has been set as the " +
                "resource pack on %s", rpName, map.getTitle()))
                .color(Style.INFO)
                .create(),
            player);
    }
    //</editor-fold>

    public void sendStatus(Player player){
        BaseComponent[] message =
            new ComponentBuilder(String.format("Map name: %s", map.getTitle()))
                .color(Style.INFO)
                .append(String.format("\nResource pack: %s", map.getResourcePack()))
                .color(Style.INFO)
                .append("\nGamemodes:")
                .color(Style.INFO)
                .append(String.format("\n  Capture The Flag: %b",
                    map.getJSONCaptureTheFlag() != null))
                .color(Style.INFO)
                .append(String.format("\n  Death Run: %b",
                    map.getJSONDeathRun() != null))
                .color(Style.INFO)
                .append(String.format("\n  Free for All: %b",
                    map.getJSONFreeForAll() != null))
                .color(Style.INFO)
                .append(String.format("\n  Infected: %b",
                    map.getJSONInfected() != null))
                .color(Style.INFO)
                .append(String.format("\n  One in the Quiver: %b",
                    map.getJSONOneInTheQuiver() != null))
                .color(Style.INFO)
                .append(String.format("\n  Ringbearer: %b",
                    map.getJSONRingBearer() != null))
                .color(Style.INFO)
                .append(String.format("\n  Team Conquest: %b",
                    map.getJSONTeamConquest() != null))
                .color(Style.INFO)
                .append(String.format("\n  Team Deathmatch: %b",
                    map.getJSONTeamDeathMatch() != null))
                .color(Style.INFO)
                .append(String.format("\n  Team Slayer: %b",
                    map.getJSONTeamSlayer() != null))
                .color(Style.INFO).create();
        sendBaseComponent(message, player);
    }
    public JSONMap getMap() {
        return map;
    }
    public void setMap(String mapName, Player player){
        map = PVPPlugin.getInstance().getMaps().get(mapName);
        gamemodeEditor = null;
        sendBaseComponent(new ComponentBuilder(String.format("Selected %s",
            mapName)).color(Style.INFO).create(), player);
    }
    public GamemodeEditor getGamemodeEditor(){
        return gamemodeEditor;
    }
    public void setGamemodeEditor(String gamemode, Player player){
        switch(gamemode){
            case Gamemodes.CAPTURETHEFLAG:
                gamemodeEditor = new CaptureTheFlagEditor(map);
                break;
            case Gamemodes.DEATHRUN:
                gamemodeEditor = new DeathRunEditor(map);
                break;
            case Gamemodes.FREEFORALL:
                gamemodeEditor = new FreeForAllEditor(map);
                break;
            case Gamemodes.INFECTED:
                gamemodeEditor = new InfectedEditor(map);
                break;
            case Gamemodes.ONEINTHEQUIVER:
                gamemodeEditor = new OneInTheQuiverEditor(map);
                break;
            case Gamemodes.RINGBEARER:
                gamemodeEditor = new RingBearerEditor(map);
                break;
            case Gamemodes.TEAMCONQUEST:
                gamemodeEditor = new TeamConquestEditor(map);
                break;
            case Gamemodes.TEAMDEATHMATCH:
                gamemodeEditor = new TeamDeathMatchEditor(map);
                break;
            case Gamemodes.TEAMSLAYER:
                gamemodeEditor = new TeamSlayerEditor(map);
                break;
        }
        sendBaseComponent(
            new ComponentBuilder(String.format("Set the gamemode to %s.\n " +
                "Current state of the gamemode is:", gamemode))
                .color(Style.INFO)
                .create(),
            player);
        gamemodeEditor.sendStatus(player);
    }
    public void showSpawns(Player player) {
        SpawnMarker(map.getSpawn(), "Map spawn");
        if(gamemodeEditor != null)
            gamemodeEditor.ShowPoints(player);
    }
    public static void hideSpawns(Player player, boolean toMessage) {
        ArmorStand toDelete;
        for(Entity marker : Bukkit.getWorld("world").getEntities())
            if(marker.getType() == EntityType.ARMOR_STAND){
                toDelete = (ArmorStand) marker;
                if(toDelete.isMarker())
                    toDelete.remove();
            }
        if(toMessage)
            sendBaseComponent(
                new ComponentBuilder("Spawns hidden.")
                    .color(Style.INFO)
                    .create(),
                player);
    }

    public static void SpawnMarker(JSONLocation loc, String name){
        ArmorStand marker =
            (ArmorStand) Bukkit.getWorld(loc.getWorld()).spawnEntity(
                LocationTranscriber.TranscribeFromJSON(loc)
                    .add(0, 1, 0),
                EntityType.ARMOR_STAND);
        marker.setGravity(false);
        marker.setCustomName(name);
        marker.setCustomNameVisible(true);
        marker.setGlowing(true);
        marker.setMarker(true);
    }
}
