package com.mcmiddleearth.mcme.pvpplugin.Util;

import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class ShortEventClass {
    public static final Class<?> ARROW_GRAB =PlayerPickupArrowEvent.class;
    public static final Class<?> SHOOT = EntityShootBowEvent.class;
    public static final Class<?> PLAYER_DEATH = PlayerDeathEvent.class;
    public static final Class<?> PLAYER_INTERACT = PlayerInteractEvent.class;
    public static final Class<?> BLOCK_DAMAGE = EntityDamageByBlockEvent.class;
    public static final Class<?> PLAYER_COMMAND = PlayerCommandPreprocessEvent.class;
    public static final Class<?> PLAYER_MOVE = PlayerMoveEvent.class;
    public static final Class<?> RESPAWN = PlayerRespawnEvent.class;
}
