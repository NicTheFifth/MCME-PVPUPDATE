package com.mcmiddleearth.mcme.pvpplugin.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.gamemodes.helpers.baseGamemode;
import com.mcmiddleearth.mcme.pvpplugin.maps.Map;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public interface gamemode {

    void Start(Map m, int parameter);

    baseGamemode.gameState getState();

    ArrayList<String> getNeededPoints();

    void End(Map m);

    boolean midgamePlayerJoin(Player p);

    String requiresParameter();

    boolean isMidgameJoin();
}
