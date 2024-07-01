package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONDeathRun;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONGamemode;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.SpecialPointEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.TeamSpawnEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class DeathRunEditor extends TeamSpawnEditor implements SpecialPointEditor {

    /**
     * SpecialPointNames is a map of &lt;name, setter of said point&gt;
     */
    Map<String, SpecialPointEditor.SetterTeleporterPair> specialPointNames = new HashMap<>();
    public DeathRunEditor(JSONMap map){
        if(map.getJSONDeathRun() == null)
            map.setJSONDeathRun(new JSONDeathRun());
        setDisplayString("Death Run");
        this.jsonGamemode = map.getJSONDeathRun();
        initSpawnNames();
        initSpecialPointNames();
    }

    public void setDeathSpawn(Player player){
        Location deathSpawn = player.getLocation();
        JSONLocation JSONDeathSpawn = new JSONLocation(deathSpawn);
        ((JSONDeathRun)jsonGamemode).setDeathSpawn(JSONDeathSpawn);
        sendBaseComponent(
            new ComponentBuilder("Death spawn set for Death Run.")
                .color(Style.INFO)
                .create(),
            player
        );
    }

    public void teleportToDeathSpawn(Player player){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONDeathRun)jsonGamemode).getDeathSpawn()));
        sendBaseComponent(
            new ComponentBuilder("Teleported to death spawn")
                .color(Style.INFO)
                .create(),
            player
        );
    }

    public void setRunnerSpawn(Player player){
        Location runnerSpawn = player.getLocation();
        JSONLocation JSONRunnerSpawn = new JSONLocation(runnerSpawn);
        ((JSONDeathRun)jsonGamemode).setRunnerSpawn(JSONRunnerSpawn);
        sendBaseComponent(
            new ComponentBuilder("Runner spawn set for Death Run.")
                .color(Style.INFO)
                .create(),
            player
        );
    }

    public void teleportToRunnerSpawn(Player player){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONDeathRun)jsonGamemode).getRunnerSpawn()));
        sendBaseComponent(
            new ComponentBuilder("Teleported to runner spawn")
                .color(Style.INFO)
                .create(),
            player
        );
    }

    public void setKillHeight(Player player){
        int height = player.getLocation().getBlockY();
        ((JSONDeathRun)jsonGamemode).setKillHeight(height);

        sendBaseComponent(
                new ComponentBuilder(String.format("Kill height set to %d for Death Run.", height))
                        .color(Style.INFO)
                        .create(),
                player
        );
    }

    public void setGoal(Player player){
        Location goal = player.getLocation();
        JSONLocation JSONGoal = new JSONLocation(goal);
        ((JSONDeathRun)jsonGamemode).setGoal(JSONGoal);

        sendBaseComponent(
            new ComponentBuilder("Goal set for Death Run.")
                .color(Style.INFO)
                .create(),
            player
        );
    }
    public void teleportToGoal(Player player){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONDeathRun)jsonGamemode).getGoal()));
        sendBaseComponent(
            new ComponentBuilder("Teleported to goal")
                .color(Style.INFO)
                .create(),
            player
        );
    }
    @Override
    public String getGamemode() {
        return Gamemodes.DEATHRUN;
    }
    @Override
    public void ShowPoints(Player player) {
        JSONLocation goal = ((JSONDeathRun)jsonGamemode).getGoal();
        MapEditor.SpawnMarker(goal, "goal");
        JSONLocation runnerSpawn = ((JSONDeathRun)jsonGamemode).getRunnerSpawn();
        MapEditor.SpawnMarker(runnerSpawn, "spawn runner");
        JSONLocation deathSpawn = ((JSONDeathRun)jsonGamemode).getDeathSpawn();
        MapEditor.SpawnMarker(deathSpawn, "spawn death");
    }
    @Override
    public void sendStatus(Player player){
        sendBaseComponent(
            new ComponentBuilder("Current selected gamemode: Death Run.")
                .color(Style.INFO)
                .append(String.format("  Max players: %d",
                    jsonGamemode.getMaximumPlayers()))
                .color(Style.INFO)
                .append(String.format("  Runner spawn set: %b",
                    ((JSONDeathRun)jsonGamemode).getRunnerSpawn()))
                .color(Style.INFO)
                .append(String.format("  Death spawn set: %b",
                    ((JSONDeathRun)jsonGamemode).getDeathSpawn()))
                .color(Style.INFO)
                .create(),
            player);
    }

    @Override
    protected void initSpawnNames() {
        getSpawnNames().put("death",
            new TeamSpawnEditor.SetterTeleporterPair(this::setDeathSpawn,
                this::teleportToDeathSpawn));
        getSpawnNames().put("runner",
            new TeamSpawnEditor.SetterTeleporterPair(this::setRunnerSpawn,
                this::teleportToRunnerSpawn));
    }

    @Override
    public void initSpecialPointNames() {
        getSpecialPointNames().put("goal",
            new SpecialPointEditor.SetterTeleporterPair(this::setGoal,
            this::teleportToGoal));
    }
    public Map<String, SpecialPointEditor.SetterTeleporterPair> getSpecialPointNames(){
        return specialPointNames;
    }
}