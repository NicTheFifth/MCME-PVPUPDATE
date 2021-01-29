package com.mcmiddleearth.mcme.pvpplugin.Handlers;

import com.google.common.collect.Lists;
import com.mcmiddleearth.mcme.pvpplugin.Util.Permission;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashSet;
import java.util.Set;

public class OnCommandRebroadcaster implements EventRebroadcaster<PlayerCommandPreprocessEvent>{
    //TODO: Use a whitelist method for blocking commands (based on permissions and gamestate)

    @Getter
    private final Set<EventListener<PlayerCommandPreprocessEvent>> eventListeners = new HashSet<>();

    private final Set<String> whitelist = new HashSet<String>(Lists.newArrayList("pvp", "world"));

    @EventHandler
    public void onPlayerCommandIssuing(PlayerCommandPreprocessEvent event){
        String[] input;
        if (eventListeners.stream().anyMatch(listener -> listener.controlsPlayer(event.getPlayer()))) {
            input = event.getMessage().split(" ");
            if (!whitelist.contains(input[0]) && !event.getPlayer().hasPermission(Permission.PVP_MANAGER)){
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.YELLOW + "You are not allowed to use this command during a game.");
            }
        }
    }

    @Override
    public void addListener(EventListener<PlayerCommandPreprocessEvent> eventListener) {
        eventListeners.add(eventListener);
    }

    @Override
    public void removeListener(EventListener<PlayerCommandPreprocessEvent> eventListener) {
        eventListeners.remove(eventListener);
    }
}
