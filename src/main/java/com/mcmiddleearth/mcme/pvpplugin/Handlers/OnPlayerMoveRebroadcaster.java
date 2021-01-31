package com.mcmiddleearth.mcme.pvpplugin.Handlers;

import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Set;

public class OnPlayerMoveRebroadcaster implements EventRebroadcaster<PlayerMoveEvent>{
    @Getter
    private final Set<EventListener<PlayerMoveEvent>> eventListeners = new HashSet<>();

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event){
        eventListeners.stream().filter(
                listener -> listener.controlsPlayer(event.getPlayer())).forEach(
                listener -> listener.handleEvent(event));
    }
    @Override
    public void addListener(EventListener<PlayerMoveEvent> eventListener) {
        eventListeners.add(eventListener);
    }

    @Override
    public void removeListener(EventListener<PlayerMoveEvent> eventListener) {
        eventListeners.remove(eventListener);
    }
}
