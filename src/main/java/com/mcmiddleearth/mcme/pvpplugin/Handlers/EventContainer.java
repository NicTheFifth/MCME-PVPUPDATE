package com.mcmiddleearth.mcme.pvpplugin.Handlers;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public abstract class EventContainer{
    EventListener<PlayerMoveEvent> playerMoveEventEventListener;
    EventListener<PlayerDeathEvent> playerDeathEventEventListener;

}
