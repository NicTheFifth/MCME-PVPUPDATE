package com.mcmiddleearth.pvpplugin.runners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public interface GamemodeListener extends Listener {
    @EventHandler
    void onPlayerDeath(PlayerDeathEvent e);
    @EventHandler
    void onPlayerLeave(PlayerQuitEvent e);
    @EventHandler
    void onPlayerMove(PlayerMoveEvent e);
}
