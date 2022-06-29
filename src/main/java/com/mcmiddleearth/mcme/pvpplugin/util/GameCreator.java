package com.mcmiddleearth.mcme.pvpplugin.util;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.exceptions.UnloadableGamemodeException;
import com.mcmiddleearth.mcme.pvpplugin.exceptions.UnloadableMapException;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes.JSONGamemode;
import com.mcmiddleearth.mcme.pvpplugin.runners.GamemodeRunner;

public class GameCreator {
    JSONMap map = null;
    Gamemodes gamemode = null;
    Integer var = null;

    public GameCreator(){}
    public GameCreator(String map){
        this.map = PVPPlugin.getInstance().getMaps().get(map);
        checkMap();
    }

    public GameCreator(String map, String gamemode){
        this.map = PVPPlugin.getInstance().getMaps().get(map);
        this.gamemode = Gamemodes.getGamemode(gamemode);
        checkMap();
        checkGamemode();
    }

    public GameCreator(String map, String gamemode, Integer var){
        this.map = PVPPlugin.getInstance().getMaps().get(map);
        this.gamemode = Gamemodes.getGamemode(gamemode);
        this.var = var;
        checkMap();
        checkGamemode();
    }

    private void checkMap(){
        if(this.map == null)
            throw new UnloadableMapException(new Exception("Map is null."));
        if(invalidMap(this.map))
            throw new UnloadableMapException(new Exception(String.format("%s is not configured", map)));
    }

    private void checkGamemode(){
        if(this.gamemode == null)
            throw new UnloadableGamemodeException(new Exception("Gamemode is null."));
        if(gamemode.invalidGamemode().apply(this.map))
            throw new UnloadableGamemodeException(new Exception(String.format("%s is not configured on %s", gamemode.getTitle(), map.getTitle())));
    }

    public String[] loadPrivate() {
        if(map == null)
            return new String[]{Style.ERROR + "No map set, please use /pvp load map <map>"};
        if(gamemode == null)
            return new String[]{Style.ERROR + "No gamemode set, please use /pvp load gamemode <gamemode>"};
        if(gamemode.needsVar()) {
            PVPPlugin.getInstance().setActiveGame(gamemode.getPrivateRunner().apply(map));
            //TODO: Broadcast
            return new String[]{Style.INFO + "Game loaded successfully."};
        }
        if(var == null){
            return new String[]{Style.ERROR + "No variable set, please use /pvp load var <number>"};
        }
        GamemodeRunner r = gamemode.getPrivateRunner().apply(map);
        r = gamemode.applyVar().apply(r,var);
        PVPPlugin.getInstance().setActiveGame(r);
        //TODO: Broadcast
        return new String[]{Style.INFO + "Game loaded successfully."};
    }

    public String[] loadPublic() {
        if(map == null)
            return new String[]{Style.ERROR + "No map set, please use /pvp load map <map>"};
        if(gamemode == null)
            return new String[]{Style.ERROR + "No gamemode set, please use /pvp load gamemode <gamemode>"};
        if(gamemode.needsVar()) {
            PVPPlugin.getInstance().setActiveGame(gamemode.getPublicRunner().apply(map));
            //TODO: Broadcast
            return new String[]{Style.INFO + "Game loaded successfully."};
        }
        if(var == null){
            return new String[]{Style.ERROR + "No variable set, please use /pvp load var <number>"};
        }
        GamemodeRunner r = gamemode.getPublicRunner().apply(map);
        r = gamemode.applyVar().apply(r,var);
        PVPPlugin.getInstance().setActiveGame(r);
        //TODO: Broadcast
        return new String[]{Style.INFO + "Game loaded successfully."};
    }

    public String[] loadMap(JSONMap map) {
        if(invalidMap(map))
            return new String[]{Style.ERROR + String.format("%s is not a configured map, please configure it first.", map.getTitle())};
        this.map = map;
        return new String[]{Style.INFO + String.format("%s is loaded.", map.getTitle())};
    }

    private boolean invalidMap(JSONMap map) {
        return map.getSpawn() == null && map.getRegionPoints().isEmpty();
    }

    public String[] loadGamemode(String gamemode) {
        if(map == null)
            return new String[]{Style.ERROR + "Can't set gamemode, first set the map, using /pvp load map <map>."};
        JSONGamemode jsonGamemode = Gamemodes.getGamemode(gamemode).getJSONGamemode().apply(map);
        if(jsonGamemode==null)
            return new String[]{Style.ERROR + String.format("%s on %s is not configured, please do so first!", gamemode, map.getTitle())};
        if(Gamemodes.getGamemode(gamemode).invalidGamemode().apply(map))
            return new String[]{Style.ERROR + String.format("%s on %s is not configured, please do so first!", gamemode, map.getTitle())};
        this.gamemode = Gamemodes.getGamemode(gamemode);
        return new String[]{Style.INFO + String.format("%s set as a gamemode for the game on %s!", gamemode, map.getTitle())};
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
