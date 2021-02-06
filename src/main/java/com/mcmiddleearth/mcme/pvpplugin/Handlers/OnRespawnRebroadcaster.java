package com.mcmiddleearth.mcme.pvpplugin.Handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashSet;
import java.util.Set;

public class OnRespawnRebroadcaster implements EventRebroadcaster<PlayerRespawnEvent> {
    private final Set<EventListener<PlayerRespawnEvent>> eventListeners = new HashSet<>();

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        eventListeners.stream().filter(listener ->
                listener.controlsPlayer(event.getPlayer())).forEach(listener ->
                listener.handleEvent(event));
    }
    @Override
    public void addListener(EventListener<PlayerRespawnEvent> eventListener) {
        eventListeners.add(eventListener);
    }

    @Override
    public void removeListener(EventListener<PlayerRespawnEvent> eventListener) {
        eventListeners.remove(eventListener);
    }
}
