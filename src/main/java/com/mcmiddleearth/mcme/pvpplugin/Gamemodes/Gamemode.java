package com.mcmiddleearth.mcme.pvpplugin.Gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.Gamemodes.helpers.baseGamemode;
import com.mcmiddleearth.mcme.pvpplugin.Handlers.EventListener;
import com.mcmiddleearth.mcme.pvpplugin.Maps.Map;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public interface Gamemode<T> extends EventListener<T> {

    void Start(Map m, int parameter);

    ArrayList<String> getNeededPoints();

    void End(Map m);

    boolean midgamePlayerJoin(Player p);

    String requiresParameter();

    boolean isMidgameJoin();
}
