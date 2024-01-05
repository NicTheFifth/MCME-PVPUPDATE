package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONFreeForAll;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class FreeForAllEditor implements GamemodeEditor,SpawnListEditor{
    JSONFreeForAll jsonFreeForAll;
    private FreeForAllEditor(){}
    public FreeForAllEditor(JSONMap map){
        if(map.getJSONFreeForAll() == null)
            map.setJSONFreeForAll(new JSONFreeForAll());
        this.jsonFreeForAll = map.getJSONFreeForAll();
    }
    @Override
    public void addSpawn(Player player){
        JSONLocation JSONSpawn = new JSONLocation(player.getLocation());
        jsonFreeForAll.getSpawns().add(JSONSpawn);
        sendBaseComponent(new ComponentBuilder("Spawn added to Free for All.")
                .color(Style.INFO)
                .create(),
            player);

    }
    @Override
    public void deleteSpawn(int toDelete, Player player){
        jsonFreeForAll.getSpawns().remove(toDelete);
        sendBaseComponent(
            new ComponentBuilder("Spawn removed from Free for All.")
                .color(Style.INFO)
                .create(),
            player);
    }

    @Override
    public void setMaxPlayers(Integer maxPlayers, Player player) {
        jsonFreeForAll.setMaximumPlayers(maxPlayers);
        sendBaseComponent(
            new ComponentBuilder(String.format("Set the max players to %d.",
                maxPlayers))
                .color(Style.INFO)
                .create(),
            player);

    }

    @Override
    public String getGamemode() {
        return Gamemodes.FREEFORALL;
    }

    @Override
    public void setMap(JSONMap map) {
        if(map.getJSONFreeForAll() == null)
            map.setJSONFreeForAll(new JSONFreeForAll());
        this.jsonFreeForAll = map.getJSONFreeForAll();

    }

    @Override
    public String[] getInfo() {
        return new String[0];
    }
    @Override
    public Integer amountOfSpawns(){
        return this.jsonFreeForAll.getSpawns().size();
    }
}
