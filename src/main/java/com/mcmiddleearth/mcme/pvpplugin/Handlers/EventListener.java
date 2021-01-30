package com.mcmiddleearth.mcme.pvpplugin.Handlers;

import org.bukkit.entity.Player;

public interface EventListener<T> {

    void handleEvent(T event);

    boolean canHandleEvent(T event);

    boolean controlsPlayer(Player player);
}
