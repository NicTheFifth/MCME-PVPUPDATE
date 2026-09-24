package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONRedBlueSpawnGamemode;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.TeamSpawnEditor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class RedBlueSpawnEditor extends TeamSpawnEditor {

    MiniMessage mm = PVPPlugin.getInstance().getMiniMessage();
    protected RedBlueSpawnEditor(){
        initSpawnNames();
    }

    public void setBlueSpawn(Player player){
        Location blueSpawn = player.getLocation();
        JSONLocation JSONBlueSpawn = new JSONLocation(blueSpawn);
        ((JSONRedBlueSpawnGamemode)jsonGamemode).setBlueSpawn(JSONBlueSpawn);
        player.sendMessage(mm.deserialize("<aqua>Blue spawn set for <title>.</aqua>",
                Placeholder.parsed("title", getDisplayString())));
    }
    public void teleportToBlueSpawn(Player player){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONRedBlueSpawnGamemode)jsonGamemode).getBlueSpawn()));
        player.sendMessage(mm.deserialize("<aqua>Teleported to blue spawn.</aqua>"));
    }
    public void setRedSpawn(Player player){
        Location redSpawn = player.getLocation();
        JSONLocation JSONRedSpawn = new JSONLocation(redSpawn);
        ((JSONRedBlueSpawnGamemode)jsonGamemode).setRedSpawn(JSONRedSpawn);
        player.sendMessage(mm.deserialize("<aqua>Red spawn set for <title>.</aqua>",
                Placeholder.parsed("title", getDisplayString())));
    }
    public void teleportToRedSpawn(Player player){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONRedBlueSpawnGamemode)jsonGamemode).getRedSpawn()));
        player.sendMessage(mm.deserialize("<aqua>Teleported to red spawn.</aqua>"));
    }
    @Override
    public void ShowPoints(Player player) {
        JSONLocation redSpawn = ((JSONRedBlueSpawnGamemode)jsonGamemode)
            .getRedSpawn();
        MapEditor.SpawnMarker(redSpawn, "spawn red");
        JSONLocation blueSpawn =
            ((JSONRedBlueSpawnGamemode)jsonGamemode).getBlueSpawn();
        MapEditor.SpawnMarker(blueSpawn, "spawn blue");

    }
    @Override
    public void sendStatus(Player player) {
        JSONRedBlueSpawnGamemode redBlueSpawn = (JSONRedBlueSpawnGamemode)jsonGamemode;
        player.sendMessage(mm.deserialize("""
                <aqua>Current selected gamemode: <gamemode>
                  Max players: <max>
                  Blue spawn set: <bs>
                  Red spawn set: <rs>""",
                Placeholder.parsed("gamemode", getDisplayString()),
                Placeholder.parsed("max", String.valueOf(redBlueSpawn.getMaximumPlayers())),
                Placeholder.parsed("bs", String.valueOf(redBlueSpawn.getBlueSpawn() != null)),
                Placeholder.parsed("rs", String.valueOf(redBlueSpawn.getRedSpawn() != null))));
    }
    protected void initSpawnNames() {
        getSpawnNames().put("red",
            new SetterTeleporterPair(this::setRedSpawn,
                this:: teleportToRedSpawn));
        getSpawnNames().put("blue",
            new SetterTeleporterPair(this::setBlueSpawn,
                this::teleportToBlueSpawn));
    }
}
