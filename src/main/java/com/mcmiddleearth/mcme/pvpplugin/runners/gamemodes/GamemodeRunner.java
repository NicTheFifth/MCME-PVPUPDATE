package com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes;

import org.bukkit.entity.Player;

import java.util.List;

public abstract class GamemodeRunner {

    BaseGamemodeRunner baseRunner;

    abstract public boolean canStart();
    abstract public void start();
    abstract public void end(boolean stopped);
    abstract public List<String> tryJoin(Player player);
    abstract public void leave(Player player, boolean failedJoin);
    abstract void initBaseRunner();
}
