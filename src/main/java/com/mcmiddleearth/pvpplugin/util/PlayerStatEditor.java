package com.mcmiddleearth.pvpplugin.util;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.Playerstat;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerStatEditor {
    //ToDo: look at moving the playerstats here and keeping them private
    //ToDo: add clear functionality here.
    public static void addDeath(Player player) {
        Playerstat playerstat = getPlayerStat(player.getUniqueId());
        playerstat.addDeath();
    }
    public static void addKill(Player player) {
        Playerstat playerstat = getPlayerStat(player.getUniqueId());
        playerstat.addKill();
    }
    public static void addPlayed(Player player) {
        Playerstat playerstat = getPlayerStat(player.getUniqueId());
        playerstat.addPlayed();
    }
    public static void addLost(Player player) {
        Playerstat playerstat = getPlayerStat(player.getUniqueId());
        playerstat.addLost();
    }
    public static void addWon(Player player) {
        Playerstat playerstat = getPlayerStat(player.getUniqueId());
        playerstat.addWon();
    }
    public static void addSpectate(Player player) {
        Playerstat playerstat = getPlayerStat(player.getUniqueId());
        playerstat.addSpectate();
    }
    public static void addDamageGiven(Player player, Integer damage) {
        Playerstat playerstat = getPlayerStat(player.getUniqueId());
        playerstat.addDamageGiven(damage);
    }
    public static void addDamageTaken(Player player, Integer damage) {
        Playerstat playerstat = getPlayerStat(player.getUniqueId());
        playerstat.addDamageTaken(damage);
    }
    private static Playerstat getPlayerStat(UUID player){
        HashMap<UUID, Playerstat> playerstats = PVPPlugin.getInstance().getPlayerstats();
        Playerstat playerstat = playerstats.get(player);
        if(playerstat == null){
            playerstat = new Playerstat();
            playerstats.put(player, playerstat);
        }
        return playerstat;
    }
}
