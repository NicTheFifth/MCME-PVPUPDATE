package com.mcmiddleearth.mcme.pvpplugin.Handlers;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.*;

public class OnArrowShootListener  implements EventRebroadcaster<EntityShootBowEvent>  {

    @Getter
    private final Set<EventListener<EntityShootBowEvent>> eventListeners = new HashSet<>();
    private final HashMap<EventListener<EntityShootBowEvent>,List<Entity>> trackedProjectiles = new HashMap<>();

    public OnArrowShootListener(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PVPPlugin.getPlugin(), despawnArrows, 0, 5);
    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {
        if (event.getEntityType().equals(EntityType.PLAYER)){
            Player player = (Player) event.getEntity();
            eventListeners.stream().filter(listener -> listener.controlsPlayer(player)).forEach(listener -> {
                List<Entity> projectiles = trackedProjectiles.get(listener);
                projectiles.add(event.getProjectile());
                trackedProjectiles.put(listener, projectiles);
            });
        }
    }

    public Runnable despawnArrows = () -> {
        trackedProjectiles.values().stream().forEach(
                projectiles -> projectiles.stream().filter(Entity::isOnGround).forEach(
                        projectile -> {projectile.remove(); projectiles.remove(projectile);}));
                };

    @Override
    public void addListener(EventListener<EntityShootBowEvent> eventListener) {
        eventListeners.add(eventListener);
    }

    @Override
    public void removeListener(EventListener<EntityShootBowEvent> eventListener) {
        trackedProjectiles.get(eventListener).forEach(Entity::remove);
        eventListeners.remove(eventListener);
    }
}
