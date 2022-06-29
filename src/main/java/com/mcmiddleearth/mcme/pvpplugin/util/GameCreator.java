package com.mcmiddleearth.mcme.pvpplugin.util;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;

public class GameCreator {
    JSONMap map;
    Gamemodes gamemode;
    Integer var;

    public GameCreator(){}
    public GameCreator(String map){
        this.map = PVPPlugin.getInstance().getMaps().get(map);
    }
    public GameCreator(String map, String gamemode){
        this.map = PVPPlugin.getInstance().getMaps().get(map);
        this.gamemode = Gamemodes.getGamemode(gamemode);
    }
    public GameCreator(String map, String gamemode, Integer var){
        this.map = PVPPlugin.getInstance().getMaps().get(map);
        this.gamemode = Gamemodes.getGamemode(gamemode);
        this.var = var;
    }

    //<editor-fold desc="Getter and Setters">
    public JSONMap getMap() {
        return map;
    }

    public void setMap(JSONMap map) {
        this.map = map;
    }

    public Gamemodes getGamemode() {
        return gamemode;
    }

    public void setGamemode(Gamemodes gamemode) {
        this.gamemode = gamemode;
    }

    public Integer getVar() {
        return var;
    }

    public void setVar(Integer var) {
        this.var = var;
    }
    //</editor-fold>
}
