package com.mcmiddleearth.pvpplugin.util;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.Playerstat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.UUID;

public class JoinListener implements Listener {
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e){
        UUID uuid = e.getPlayer().getUniqueId();
        HashMap<UUID, Playerstat> playerstats = PVPPlugin.getInstance().getPlayerstats();
        if(playerstats.get(uuid) == null)
            playerstats.put(uuid, new Playerstat());
    }
}