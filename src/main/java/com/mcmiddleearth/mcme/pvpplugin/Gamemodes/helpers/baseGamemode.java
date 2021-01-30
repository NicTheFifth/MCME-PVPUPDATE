package com.mcmiddleearth.mcme.pvpplugin.Gamemodes.helpers;

import com.google.common.collect.Lists;
import com.mcmiddleearth.mcme.pvpplugin.Gamemodes.Gamemode;
import com.mcmiddleearth.mcme.pvpplugin.Maps.Map;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.Util.ShortEventClass;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class baseGamemode implements Gamemode {
    @Getter
    private final ArrayList<Player> spectators = new ArrayList<>();
    @Getter
    private final ArrayList<Player> players = new ArrayList<>();
    @Getter
    private ArrayList<Player> winners = new ArrayList<>();
    @Getter
    ArrayList<Player> deadPlayers = new ArrayList<>();
    Set<Class<?>> importantEvents = new HashSet<Class<?>>(
            Lists.newArrayList(ShortEventClass.ARROW_GRAB, ShortEventClass.BLOCK_DAMAGE, ShortEventClass.PLAYER_COMMAND, ShortEventClass.PLAYER_INTERACT));

    @Override
    public void Start(Map m, int parameter) {
        importantEvents.forEach(event -> PVPPlugin.addEventListener(event, this));
    }

    @Override
    public void End(Map m) {
        players.forEach(player -> {
            if (winners.contains(player)) {
                PVPPlugin.getPlayerStats().get(player.getUniqueId()).addGameWon();
            } else {
                PVPPlugin.getPlayerStats().get(player.getUniqueId()).addGameLost();
            }
            PVPPlugin.getPlayerStats().get(player.getUniqueId()).addPlayedGame();
        });
        spectators.forEach(player ->{PVPPlugin.getPlayerStats().get(player.getUniqueId()).addGameSpectated();});
        spectators.clear();
        deadPlayers.clear();
        players.clear();
        winners.clear();
        importantEvents.forEach(event -> PVPPlugin.removeEventListener(event, this));
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
        PVPPlugin.getPlayerStats().get(killed.getUniqueId()).addDeath();
        Player killer = event.getEntity().getKiller();
        if(killer != null) {
            PVPPlugin.getPlayerStats().get(killer.getUniqueId()).addKill();
        }
    }

    @Override
    public boolean canHandleEvent(Object event) { return importantEvents.contains(event); }

    @Override
    public boolean controlsPlayer(Player player) { return players.contains(player); }
}
