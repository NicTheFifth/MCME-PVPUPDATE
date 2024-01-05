package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONDeathRun;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class DeathRunEditor implements GamemodeEditor{
    JSONDeathRun jsonDeathRun;
    private DeathRunEditor(){}
    public DeathRunEditor(JSONMap map){
        if(map.getJSONDeathRun() == null)
            map.setJSONDeathRun(new JSONDeathRun());
        this.jsonDeathRun = map.getJSONDeathRun();
    }
    @Override
    public void setMaxPlayers(Integer maxPlayers, Player player) {
        jsonDeathRun.setMaximumPlayers(maxPlayers);
        sendBaseComponent(
            new ComponentBuilder(String.format("Set the max players to %d.",
                maxPlayers))
                .color(Style.INFO)
                .create(),
            player);
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
    public void setGoal(Player player){
        Location goal = player.getLocation();
        JSONLocation JSONGoal = new JSONLocation(goal);
        jsonDeathRun.setGoal(JSONGoal);

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
    public void setMap(JSONMap map) {
        if(map.getJSONDeathRun() == null)
            map.setJSONDeathRun(new JSONDeathRun());
        this.jsonDeathRun = map.getJSONDeathRun();
    }

    @Override
    public String[] getInfo(){
        return new String[]{
                String.format(Style.INFO + "Current selected gamemode: Death Run."),
                String.format(Style.INFO + "Max players: %d", jsonDeathRun.getMaximumPlayers()),
                String.format(Style.INFO + "Runner spawn set: %b", jsonDeathRun.getRunnerSpawn()),
                String.format(Style.INFO + "Death spawn set: %b", jsonDeathRun.getDeathSpawn())
        };
    }
}