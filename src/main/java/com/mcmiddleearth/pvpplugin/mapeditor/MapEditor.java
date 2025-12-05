package com.mcmiddleearth.pvpplugin.mapeditor;

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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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


public class MapEditor {

    JSONMap map;
    GamemodeEditor gamemodeEditor;
    MiniMessage mm = PVPPlugin.getInstance().getMiniMessage();

    public MapEditor(JSONMap map, Player player){
        this.map = map;
        player.sendMessage(mm.deserialize(
                "Map editor for <title> created.",
                Placeholder.parsed("title", map.getTitle())));
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
            Component message;
            if(r.getHeight() < 250){
                message = Component.text("Use //expand vert then try again!").style(net.kyori.adventure.text.format.Style.style(NamedTextColor.RED));
            }
            else{
                List<BlockVector2> wePoints = r.polygonize(1000);
                ArrayList<JSONLocation> bPoints = new ArrayList<>();

                for(BlockVector2 point : wePoints){
                    bPoints.add(new JSONLocation(new Location(source.getWorld(), point.x(), 1, point.z())));
                }

                map.setRegionPoints(bPoints);
                message = mm.deserialize(
                        "<aqua>Area set for <title>!</aqua>",
                        Placeholder.parsed("title", map.getTitle()));
            }
            source.sendMessage(message);
        }
        catch(IncompleteRegionException e){
            source.sendMessage(mm.deserialize(
                    "<red>You don't have a region selected!</red>"
            ));
        }
    }

    public void setTitle(String newName, Player player) {
        String oldName = map.getTitle();
        PVPPlugin.getInstance().getMaps().remove(oldName);
        File f = new File(PVPPlugin.getInstance().getMapDirectory() +
                FileSystems.getDefault().getSeparator() +
                oldName + ".json");
        map.setTitle(newName);
        PVPPlugin.getInstance().getMaps().put(newName, map);
        try {
            if(!f.delete())
                PVPPlugin.getInstance().getLogger().warning("File " + oldName + ".json has not been deleted!");
            player.sendMessage(mm.deserialize(
                    "<aqua><old_name> has been renamed to <new_name>.</aqua>",
                    Placeholder.parsed("old_name", oldName),
                    Placeholder.parsed("new_name", newName)
            ));
        }
        catch(SecurityException e){
            player.sendMessage(mm.deserialize(
                    "<red><old_name> couldn't be deleted.</red>",
                    Placeholder.parsed("old_name", oldName)
            ));
            Logger.getLogger("PVPPlugin").log(Level.WARNING,
                String.format("%s couldn't be deleted.", oldName));
        }
    }

    public void setSpawn(Player player) {
        Location location = player.getLocation();
        map.setSpawn(new JSONLocation(location));
        player.sendMessage(mm.deserialize("<aqua>Spawn location set for <title>!</aqua>",
                Placeholder.parsed("title", map.getTitle())));
    }

    public void setRP(String rpName, Player player) {
        map.setResourcePack(rpName);
        player.sendMessage(mm.deserialize("<aqua><rp> has been set as the resource pack for <title>!</aqua>",
                Placeholder.parsed("title", map.getTitle()),
                Placeholder.parsed("rp", rpName)));
    }
    //</editor-fold>

    public void sendStatus(Player player){
        player.sendMessage(mm.deserialize(
                """
                        <aqua>Map name: <title>
                        Resource pack: <rp>
                        Spawn set: <spawn>
                        Area set: <region>
                        Gamemodes:
                          Capture The Flag: <ctf>
                          Death Run: <dr>
                          Free for All: <ffa>
                          Infected: <in>
                          One in the Quiver: <oitq>
                          Ringbearer: <rb>
                          Team Conquest: <tc>
                          Team Deathmatch: <tdm>
                          Team Slayer: <ts></aqua>""",
                Placeholder.parsed("title", map.getTitle()),
                Placeholder.parsed("rp", map.getResourcePack()),
                Placeholder.parsed("region", String.valueOf(map.getRegionPoints() != null)),
                Placeholder.parsed("spawn", String.valueOf(map.getSpawn() != null)),
                Placeholder.parsed("ctf", String.valueOf(map.getJSONCaptureTheFlag() != null)),
                Placeholder.parsed("dr", String.valueOf(map.getJSONDeathRun() != null)),
                Placeholder.parsed("ffa", String.valueOf(map.getJSONFreeForAll() != null)),
                Placeholder.parsed("in", String.valueOf(map.getJSONInfected() != null)),
                Placeholder.parsed("oitq", String.valueOf(map.getJSONOneInTheQuiver() != null)),
                Placeholder.parsed("rb", String.valueOf(map.getJSONRingBearer() != null)),
                Placeholder.parsed("tc", String.valueOf(map.getJSONTeamConquest() != null)),
                Placeholder.parsed("tdm", String.valueOf(map.getJSONTeamDeathMatch() != null)),
                Placeholder.parsed("ts", String.valueOf(map.getJSONTeamSlayer() != null))
                ));
    }

    public JSONMap getMap() {
        return map;
    }

    public void setMap(String mapName, Player player){
        map = PVPPlugin.getInstance().getMaps().get(mapName);
        gamemodeEditor = null;
        player.sendMessage(mm.deserialize("<aqua>Selected <title>!</aqua>",
                Placeholder.parsed("title", map.getTitle())));
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
        player.sendMessage(mm.deserialize("<aqua>Set the gamemode to <gamemode>!</aqua>",
                Placeholder.parsed("gamemode", gamemode)));
        gamemodeEditor.sendStatus(player);
    }

    public void showSpawns(Player player) {
        try {
            SpawnMarker(map.getSpawn(), "Map spawn");
        } catch (NullPointerException e){
            //TODO: Fix messaging
            player.sendMessage("Something went wrong!");
        }
        if(gamemodeEditor != null)
            gamemodeEditor.ShowPoints(player);
    }

    public static void hideSpawns(Player player, boolean toMessage) {
        ArmorStand toDelete;
        World world = Bukkit.getWorld("world");
        if(world == null) {
            PVPPlugin.getInstance().getLogger().severe("Couldn't load world, error in MapEditor, line 218!");
            player.sendMessage("Something went wrong, please send in dev-public" +
                    "\"I tried to hide spawns, but it threw an error\"");
            return;
        }
        for(Entity marker : world.getEntities())
            if(marker.getType() == EntityType.ARMOR_STAND){
                toDelete = (ArmorStand) marker;
                if(toDelete.isMarker())
                    toDelete.remove();
            }
        if(toMessage)
            player.sendMessage(PVPPlugin.getInstance().getMiniMessage().deserialize("<aqua>Spawns hidden.</aqua>"));
    }

    public static void SpawnMarker(JSONLocation loc, String name){
        ArmorStand marker =
            (ArmorStand) Bukkit.getWorld(loc.getWorld()).spawnEntity(
                LocationTranscriber.TranscribeFromJSON(loc)
                    .add(0, 1, 0),
                EntityType.ARMOR_STAND);
        marker.setGravity(false);
        marker.customName(PVPPlugin.getInstance().getMiniMessage().deserialize(name));
        marker.setCustomNameVisible(true);
        marker.setGlowing(true);
        marker.setMarker(true);
    }
}
