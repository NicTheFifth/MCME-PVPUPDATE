package com.mcmiddleearth.mcme.pvpplugin.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.Handlers.EventListener;
import com.mcmiddleearth.mcme.pvpplugin.Maps.Map;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import org.bukkit.entity.Player;

import java.util.List;

public interface Gamemode<T> extends EventListener<T> {
    void start(Map m, PVPPlugin plugin);
    List<String> getNeededPoints();
    void end();
    boolean midgamePlayerJoin(Player p);
    void onPlayerJoin(Player p);
    void onPlayerLeave(Player p);
    String requiresParameter();
    boolean isMidgameJoin();
}
