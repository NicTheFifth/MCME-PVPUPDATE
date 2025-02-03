package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONInfected;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.TeamSpawnEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class InfectedEditor extends TeamSpawnEditor {
    MiniMessage mm = PVPPlugin.getInstance().getMiniMessage();
    public InfectedEditor(JSONMap map){
        if(map.getJSONInfected() == null)
            map.setJSONInfected(new JSONInfected());
        setDisplayString("Infected");
        this.jsonGamemode = map.getJSONInfected();
        initSpawnNames();
    }
    public void setInfectedSpawn(Player player){
        Location infectedSpawn = player.getLocation();
        JSONLocation JSONInfectedSpawn = new JSONLocation(infectedSpawn);
        ((JSONInfected)jsonGamemode).setInfectedSpawn(JSONInfectedSpawn);
        player.sendMessage("<aqua>Infected spawn set for Infected.</aqua>");
    }
    public void teleportToInfectedSpawn(Player player){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONInfected)jsonGamemode).getInfectedSpawn()));
        player.sendMessage("<aqua>Teleported to infected spawn.</aqua>");
    }
    public void setSurvivorSpawn(Player player){
        Location survivorSpawn = player.getLocation();
        JSONLocation JSONSurvivorSpawn = new JSONLocation(survivorSpawn);
        ((JSONInfected)jsonGamemode).setSurvivorSpawn(JSONSurvivorSpawn);
        player.sendMessage("<aqua>Survivor spawn set for Infected.</aqua>");
    }
    public void teleportToSurvivorSpawn(Player player){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONInfected)jsonGamemode).getSurvivorSpawn()));
        player.sendMessage("<aqua>Teleported to survivor spawn.</aqua>");
    }
    public String getGamemode(){return Gamemodes.INFECTED;}
    @Override
    public void ShowPoints(Player player) {
        JSONLocation infectedSpawn =
            ((JSONInfected)jsonGamemode).getInfectedSpawn();
        MapEditor.SpawnMarker(infectedSpawn, "spawn infected");
        JSONLocation survivorSpawn =
            ((JSONInfected)jsonGamemode).getSurvivorSpawn();
        MapEditor.SpawnMarker(survivorSpawn, "spawn survivor");

    }
    @Override
    public void sendStatus(Player player){
        JSONInfected infected = (JSONInfected) jsonGamemode;
        player.sendMessage(mm.deserialize("""
                <aqua>Current selected gamemode: Infected.
                  Max players: <max>
                  Survivor spawn set: <ss>
                  Infected spawn set: <is></aqua>""",
                Placeholder.parsed("max", String.valueOf(infected.getMaximumPlayers())),
                Placeholder.parsed("ss", String.valueOf(infected.getSurvivorSpawn() != null)),
                Placeholder.parsed("is", String.valueOf(infected.getInfectedSpawn() != null))
                ));
    }

    @Override
    protected void initSpawnNames() {
        getSpawnNames().put("infected",
            new SetterTeleporterPair(this::setInfectedSpawn,
                this::teleportToInfectedSpawn));
        getSpawnNames().put("survivor",
            new SetterTeleporterPair(this::setSurvivorSpawn,
                this::teleportToSurvivorSpawn));
    }
}
