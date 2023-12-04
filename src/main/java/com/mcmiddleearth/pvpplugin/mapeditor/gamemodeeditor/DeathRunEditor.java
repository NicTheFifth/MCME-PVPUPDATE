package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONDeathRun;
import org.bukkit.Location;

public class DeathRunEditor implements GamemodeEditor{
    JSONDeathRun jsonDeathRun;
    private DeathRunEditor(){}
    public DeathRunEditor(JSONMap map){
        this.jsonDeathRun = map.getJSONDeathRun();
    }
    @Override
    public String[] setMaxPlayers(Integer maxPlayers) {
        jsonDeathRun.setMaximumPlayers(maxPlayers);
        return new String[]{String.format(Style.INFO + "Set the max players to %d.", maxPlayers)};
    }
    public String[] setDeathSpawn(Location deathSpawn){
        JSONLocation JSONDeathSpawn = new JSONLocation(deathSpawn);
        jsonDeathRun.setDeathSpawn(JSONDeathSpawn);
        return new String[]{Style.INFO + "Death spawn set for Death Run."};
    }
    public String[] setRunnerSpawn(Location runnerSpawn){
        JSONLocation JSONRunnerSpawn = new JSONLocation(runnerSpawn);
        jsonDeathRun.setRunnerSpawn(JSONRunnerSpawn);
        return new String[]{Style.INFO + "Runner spawn set for Death Run."};
    }
    public String[] setGoal(Location goal){
        JSONLocation JSONGoal = new JSONLocation(goal);
        jsonDeathRun.setGoal(JSONGoal);
        return new String[]{Style.INFO + "Goal set for Death Run."};
    }
    @Override
    public String getGamemode() {
        return "Death Run";
    }
}