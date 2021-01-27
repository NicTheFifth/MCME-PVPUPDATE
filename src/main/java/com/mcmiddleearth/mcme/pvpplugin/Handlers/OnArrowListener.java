package com.mcmiddleearth.mcme.pvpplugin.Handlers;

import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupArrowEvent;

import java.util.HashSet;
import java.util.Set;

public class OnArrowListener implements EventRebroadcaster<PlayerPickupArrowEvent>  {

    @Getter
    private final Set<EventListener<PlayerPickupArrowEvent>> eventListeners = new HashSet<>();

    @EventHandler
    public void onArrowPickup(PlayerPickupArrowEvent event) {
        if (eventListeners.stream().anyMatch(listener -> listener.controlsPlayer(event.getPlayer()))){
            event.setCancelled(true);
        }
    }
//        eventListeners.stream().filter(controlsPlayer(event.player)).foreach();
//        event.cancel();
//        eventListener.foreach(listener -> listener.handle(event));

    @Override
    public void addListener(EventListener<PlayerPickupArrowEvent> eventListener) {
        eventListeners.add(eventListener);
    }

    @Override
    public void removeListener(EventListener<PlayerPickupArrowEvent> eventListener) {
        eventListeners.remove(eventListener);
    }
}
