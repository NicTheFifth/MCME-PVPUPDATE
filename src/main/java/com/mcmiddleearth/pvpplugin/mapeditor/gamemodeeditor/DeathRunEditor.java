package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONDeathRun;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.SpecialPointEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.TeamSpawnEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;


public class DeathRunEditor extends TeamSpawnEditor implements SpecialPointEditor {

    /**
     * SpecialPointNames is a map of &lt;name, setter of said point&gt;
     */
    MiniMessage mm = PVPPlugin.getInstance().getMiniMessage();
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
        player.sendMessage(mm.deserialize("<aqua>Death spawn set for Death Run.</aqua>"));
    }

    public void teleportToDeathSpawn(Player player){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONDeathRun)jsonGamemode).getDeathSpawn()));
        player.sendMessage(mm.deserialize("<aqua>Teleported to death spawn.</aqua>"));
    }

    public void setRunnerSpawn(Player player){
        Location runnerSpawn = player.getLocation();
        JSONLocation JSONRunnerSpawn = new JSONLocation(runnerSpawn);
        ((JSONDeathRun)jsonGamemode).setRunnerSpawn(JSONRunnerSpawn);
        player.sendMessage(mm.deserialize("<aqua>Runner spawn set for Death Run.</aqua>"));
    }

    public void teleportToRunnerSpawn(Player player){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONDeathRun)jsonGamemode).getRunnerSpawn()));
        player.sendMessage(mm.deserialize("<aqua>Teleported to runner spawn.</aqua>"));
    }

    public void setKillHeight(Player player){
        int height = player.getLocation().getBlockY();
        ((JSONDeathRun)jsonGamemode).setKillHeight(height);
        player.sendMessage(mm.deserialize(
                "<aqua>Kill height set to <height> for Death Run.</aqua>",
                Placeholder.parsed("height", String.valueOf(height))));
    }

    public void setGoal(Player player){
        Location goal = player.getLocation();
        JSONLocation JSONGoal = new JSONLocation(goal);
        ((JSONDeathRun)jsonGamemode).setGoal(JSONGoal);
        player.sendMessage(mm.deserialize("<aqua>Goal set for Death Run.</aqua>"));
    }
    public void teleportToGoal(Player player){
        player.teleport(
            LocationTranscriber.TranscribeFromJSON(
                ((JSONDeathRun)jsonGamemode).getGoal()));
        player.sendMessage(mm.deserialize("<aqua>Teleported to goal.</aqua>"));
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
        JSONDeathRun deathRun = (JSONDeathRun) jsonGamemode;
        player.sendMessage(mm.deserialize("""
                <aqua>Current selected gamemode: Death Run.
                  Max players: <max>
                  Runner spawn set: <rs>
                  Death spawn set: <ds>
                  Goal set: <goal>
                  Kill height: <kh></aqua>""",
                Placeholder.parsed("max", String.valueOf(deathRun.getMaximumPlayers())),
                Placeholder.parsed("rs", String.valueOf(deathRun.getRunnerSpawn() != null)),
                Placeholder.parsed("ds", String.valueOf(deathRun.getDeathSpawn() != null)),
                Placeholder.parsed("goal", String.valueOf(deathRun.getGoal() != null)),
                Placeholder.parsed("kh", String.valueOf(deathRun.getKillHeight()))
                ));
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