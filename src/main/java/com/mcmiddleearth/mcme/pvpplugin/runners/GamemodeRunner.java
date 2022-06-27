package com.mcmiddleearth.mcme.pvpplugin.runners;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;


public interface GamemodeRunner extends Listener {

    boolean canStart();

    void start();

    void end(boolean stopped);

    List<String> tryJoin(Player player);

    void leave(Player player, boolean failedJoin);
}
