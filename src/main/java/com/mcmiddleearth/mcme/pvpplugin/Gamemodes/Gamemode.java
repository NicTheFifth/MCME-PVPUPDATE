package com.mcmiddleearth.mcme.pvpplugin.Gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.Gamemodes.helpers.baseGamemode;
import com.mcmiddleearth.mcme.pvpplugin.Maps.Map;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public interface Gamemode {

    void Start(Map m, int parameter);

    baseGamemode.gameState getState();

    ArrayList<String> getNeededPoints();

    void End(Map m);

    boolean midgamePlayerJoin(Player p);

    String requiresParameter();

    boolean isMidgameJoin();
}
