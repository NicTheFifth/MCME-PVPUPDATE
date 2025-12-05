package com.mcmiddleearth.pvpplugin.runners.listeners;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public abstract class GamemodeListener implements Listener {
    GamemodeRunner runner = null;

    private GamemodeListener(){
    }
    public GamemodeListener(@NotNull GamemodeRunner runner){
        this.runner = runner;
        PVPPlugin.addEventListener(this);
    }

    abstract public void unregister();
}
