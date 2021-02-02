package com.mcmiddleearth.mcme.pvpplugin.Gamemodes.helpers;

import com.google.common.collect.Lists;
import com.mcmiddleearth.mcme.pvpplugin.Gamemodes.Gamemode;
import com.mcmiddleearth.mcme.pvpplugin.Maps.Map;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.Util.ShortEventClass;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseGamemode implements Gamemode {
    @Getter
    private final ArrayList<Player> spectators = new ArrayList<>();
    @Getter
    private final ArrayList<Player> players = new ArrayList<>();
    @Getter
    @Setter
    private Integer maxPlayers;
    @Getter
    private ArrayList<Player> winners = new ArrayList<>();
    @Getter
    private final Set<Class<?>> importantEvents = new HashSet<Class<?>>(
            Lists.newArrayList(ShortEventClass.PLAYER_MOVE, ShortEventClass.ARROW_GRAB, ShortEventClass.BLOCK_DAMAGE, ShortEventClass.PLAYER_COMMAND, ShortEventClass.PLAYER_INTERACT));
    @Getter
    private Region mapRegion;
    private final HashMap<String, Long> lastOutOfBounds = new HashMap<>();
    private PVPPlugin pvpPlugin;

    @Override
    public void start(Map m, PVPPlugin plugin) {
        pvpPlugin = plugin;
        mapRegion = m.getRegion();
        //importantEvents.forEach(event -> PVPPlugin.addEventListener(event, this));
    }

    @Override
    public void end() {
        players.forEach(player -> {
            if (winners.contains(player)) {
                pvpPlugin.getPlayerStats().get(player.getUniqueId()).addGameWon();
            } else {
                pvpPlugin.getPlayerStats().get(player.getUniqueId()).addGameLost();
            }
            pvpPlugin.getPlayerStats().get(player.getUniqueId()).addPlayedGame();
        });
        spectators.forEach(player -> pvpPlugin.getPlayerStats().get(player.getUniqueId()).addGameSpectated());
        spectators.clear();
        players.clear();
        winners.clear();
    }

    @Override
    public boolean midgamePlayerJoin(Player p) {
        return false;
    }

    @Override
    public String requiresParameter() {
        return null;
    }

    @Override
    public boolean isMidgameJoin() {
        return false;
    }

    public void handleEvent(EntityDamageByBlockEvent event) {

        if(event.getDamager().getType().equals(Material.CACTUS)){
            event.setCancelled(true);
        }
    }

    public void handleEvent(PlayerDeathEvent event){
        Player killed = event.getEntity();
        pvpPlugin.getPlayerStats().get(killed.getUniqueId()).addDeath();
        Player killer = event.getEntity().getKiller();
        if(killer != null) {
            pvpPlugin.getPlayerStats().get(killer.getUniqueId()).addKill();
        }
    }

    public void handleEvent(PlayerMoveEvent e) {
        Location to = e.getTo();

        if (!mapRegion.contains(BlockVector3.at(to.getX(), to.getY(), to.getZ()))) {
            e.setCancelled(true);

            if (!lastOutOfBounds.containsKey(e.getPlayer().getName())) {
                e.getPlayer().sendMessage(ChatColor.RED + "You aren't allowed to leave the map!");
                lastOutOfBounds.put(e.getPlayer().getName(), System.currentTimeMillis());
            } else if (System.currentTimeMillis() - lastOutOfBounds.get(e.getPlayer().getName()) > 3000) {
                e.getPlayer().sendMessage(ChatColor.RED + "You aren't allowed to leave the map!");
                lastOutOfBounds.put(e.getPlayer().getName(), System.currentTimeMillis());
            }
        }
    }

    @Override
    public boolean canHandleEvent(Object event) { return importantEvents.contains(event); }

    @Override
    public boolean controlsPlayer(Player player) { return players.contains(player); }
}
