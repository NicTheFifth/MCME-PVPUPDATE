package com.mcmiddleearth.mcme.pvpplugin.runners;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


public interface GamemodeRunner extends Listener {

    boolean canStart();

    void start();

    void run();

    void end(boolean stopped);

    String[] join(Player player);

    void leave(Player player);
}
