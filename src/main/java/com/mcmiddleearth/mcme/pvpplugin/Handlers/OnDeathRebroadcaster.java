package com.mcmiddleearth.mcme.pvpplugin.Handlers;

import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashSet;
import java.util.Set;

public class OnDeathRebroadcaster implements EventRebroadcaster<PlayerDeathEvent> {
    private final Set<EventListener<PlayerDeathEvent>> eventListeners = new HashSet<>();

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event){
        eventListeners.stream().filter(
                listener -> listener.controlsPlayer(event.getEntity())).forEach(
                        listener -> listener.handleEvent(event));
    }
    @Override
    public void addListener(EventListener<PlayerDeathEvent> eventListener) {
        eventListeners.add(eventListener);
    }

    @Override
    public void removeListener(EventListener<PlayerDeathEvent> eventListener) {
        eventListeners.remove(eventListener);
    }
}
