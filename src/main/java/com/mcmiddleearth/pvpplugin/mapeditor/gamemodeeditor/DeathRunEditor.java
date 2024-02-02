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

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class DeathRunEditor extends TeamSpawnEditor implements SpecialPointEditor {
    private DeathRunEditor(){}
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
    public String[] getInfo(){
        return new String[]{
                String.format(Style.INFO + "Current selected gamemode: Death Run."),
                String.format(Style.INFO + "Max players: %d",
                    jsonGamemode.getMaximumPlayers()),
                String.format(Style.INFO + "Runner spawn set: %b",
                    ((JSONDeathRun)jsonGamemode).getRunnerSpawn()),
                String.format(Style.INFO + "Death spawn set: %b",
                    ((JSONDeathRun)jsonGamemode).getDeathSpawn())
        };
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
}