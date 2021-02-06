package com.mcmiddleearth.mcme.pvpplugin.Handlers;

import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.Set;

public class OnPlayerInteractRebroadcaster implements EventRebroadcaster<PlayerInteractEvent> {
    private final Set<EventListener<PlayerInteractEvent>> eventListeners = new HashSet<>();

    @EventHandler
    public void onPlayerDeathEvent(PlayerInteractEvent event){
        eventListeners.stream().filter(
                listener -> listener.controlsPlayer(event.getPlayer())).forEach(
                listener -> listener.handleEvent(event));
    }
    @Override
    public void addListener(EventListener<PlayerInteractEvent> eventListener) {
        eventListeners.add(eventListener);
    }

    @Override
    public void removeListener(EventListener<PlayerInteractEvent> eventListener) {
        eventListeners.remove(eventListener);
    }
}
