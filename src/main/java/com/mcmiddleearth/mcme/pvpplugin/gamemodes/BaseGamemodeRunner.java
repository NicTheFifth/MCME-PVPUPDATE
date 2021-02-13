package com.mcmiddleearth.mcme.pvpplugin.gamemodes;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mcmiddleearth.mcme.pvpplugin.Handlers.EventListener;
import com.mcmiddleearth.mcme.pvpplugin.Maps.MapManager;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.json.JSONMap;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class BaseGamemodeRunner implements Listener {

    @Getter
    private final PVPPlugin plugin;
    private final Region region;

    @Getter
    private GamemodeState gamemodeState = GamemodeState.LOBBY;

    @Getter
    private Set<Player> players;

    private final Cache<Player, Object> playerOutOfBounds = CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.SECONDS).build();

    // Abstract Methods
    protected abstract void prepareStart();
    protected abstract void start();
    protected abstract void end();

    protected abstract boolean handlePlayerMoveEvent(PlayerMoveEvent playerMoveEvent);

    // Base Code
    protected BaseGamemodeRunner(JSONMap map, PVPPlugin plugin) {
        this.plugin = plugin;
        this.region = MapManager.convertMapToRegion(map);
    }

    public void transitionToState(GamemodeState state) {
        switch (state) {
            case COUNTDOWN:
                transistionToCountdown();
                break;
            case RUNNING:
                transitionToRunning();
                break;
            case ENDING:
                transitionToEnding();
                break;
        }
    }

    private void transistionToCountdown() {
        this.gamemodeState = GamemodeState.COUNTDOWN;
        this.registerListeners();

        this.prepareStart();
        new BukkitRunnable(){
            @Override
            public void run() {
                transitionToState(GamemodeState.RUNNING);
            }
        }.runTaskLater(plugin,5);
    }

    private void transitionToRunning() {
        this.gamemodeState = GamemodeState.RUNNING;
        start();
    }

    private void transitionToEnding() {
        this.gamemodeState = GamemodeState.ENDING;
        this.unregisterListeners();

        this.end();
    }

    private void registerListeners() {
        plugin.getPluginManager().registerEvents(this,plugin);
    }

    private void unregisterListeners() {
        PlayerMoveEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void handleEvent(PlayerMoveEvent event) {
        if (getGamemodeState() == GamemodeState.LOBBY) {
            return;
        }
        if (getGamemodeState() == GamemodeState.COUNTDOWN) {
            event.setCancelled(true);
        } else if (handlePlayerMoveEvent(event)) {
            Location newLocation = event.getTo();

            if (newLocation != null && !region.contains(BlockVector3.at(newLocation.getX(), newLocation.getY(), newLocation.getZ()))) {
                event.setCancelled(true);

                if (playerOutOfBounds.getIfPresent(event.getPlayer()) != null) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You aren't allowed to leave the map!");
                    playerOutOfBounds.put(event.getPlayer(), new Object());
                }
            }
        }
    }
}
