package com.mcmiddleearth.mcme.pvpplugin.Gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.Handlers.EventListener;
import com.mcmiddleearth.mcme.pvpplugin.Maps.Map;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.Util.JSON.JSONMap;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public interface Gamemode<T> extends EventListener<T> {
    void start(JSONMap m, PVPPlugin plugin);
    void end();
    boolean midgamePlayerJoin(Player p);
    void onPlayerJoin(Player p);
    void onPlayerLeave(Player p);
    String requiresParameter();
    boolean isMidgameJoin();
}
