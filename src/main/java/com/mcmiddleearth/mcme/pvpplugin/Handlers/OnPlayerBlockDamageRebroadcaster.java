package com.mcmiddleearth.mcme.pvpplugin.Handlers;

import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByBlockEvent;

import java.util.HashSet;
import java.util.Set;

public class OnPlayerBlockDamageRebroadcaster implements EventRebroadcaster<EntityDamageByBlockEvent>{
    @Getter
    private final Set<EventListener<EntityDamageByBlockEvent>> eventListeners = new HashSet<>();

    @EventHandler
    public void onBlockDamage(EntityDamageByBlockEvent event){
        if (event.getEntityType().equals(EntityType.PLAYER))
            eventListeners.stream().filter(
                listener -> listener.controlsPlayer((Player) event.getEntity())).forEach(
                listener -> listener.handleEvent(event));
    }
    @Override
    public void addListener(EventListener<EntityDamageByBlockEvent> eventListener) {
        eventListeners.add(eventListener);
    }

    @Override
    public void removeListener(EventListener<EntityDamageByBlockEvent> eventListener) {
        eventListeners.remove(eventListener);
    }
}
