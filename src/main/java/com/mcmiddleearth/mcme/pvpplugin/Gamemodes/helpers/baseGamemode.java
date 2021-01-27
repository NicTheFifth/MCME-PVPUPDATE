package com.mcmiddleearth.mcme.pvpplugin.Gamemodes.helpers;

import com.mcmiddleearth.mcme.pvpplugin.Gamemodes.Gamemode;
import com.mcmiddleearth.mcme.pvpplugin.Maps.Map;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class baseGamemode implements Gamemode {

    public enum gameState { IDLE, COUNTDOWN, RUNNING}

    @Getter
    ArrayList<Player> players = new ArrayList<>();

    @Override
    public void Start(Map m, int parameter) {

    }

    @Override
    public gameState getState() {
        return null;
    }

    @Override
    public ArrayList<String> getNeededPoints() {
        return null;
    }

    @Override
    public void End(Map m) {

    }

    @Override
    public boolean midgamePlayerJoin(Player p) {
        return false;
    }

    @Override
    public String requiresParameter() {
        return null;
    }

    @Override
    public boolean isMidgameJoin() {
        return false;
    }


}
