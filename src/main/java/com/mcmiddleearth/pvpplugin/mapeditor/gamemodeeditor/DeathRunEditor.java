package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONDeathRun;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.SpecialPointEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.TeamSpawnEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class DeathRunEditor extends TeamSpawnEditor implements SpecialPointEditor {

    /**
     * SpecialPointNames is a map of &lt;name, setter of said point&gt;
     */
    Map<String, Consumer<Player>> specialPointNames = new HashMap<>();
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
    @Override
    public String getGamemode() {
        return Gamemodes.DEATHRUN;
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
        getSpawnNames().put("death", this::setDeathSpawn);
        getSpawnNames().put("runner", this::setRunnerSpawn);
    }

    @Override
    public void initSpecialPointNames() {
        getSpecialPointNames().put("goal", this::setGoal);
    }
    public Map<String, Consumer<Player>> getSpecialPointNames(){
        return specialPointNames;
    }
}