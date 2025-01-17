package com.mcmiddleearth.pvpplugin.runners.runnerUtil;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

abstract class ChatHandler extends PlaceholderExpansion {
    abstract Function<Player, ChatColor> getChatColor();
    abstract Function<Player, String> getPrefix();
    abstract Function<Player, Boolean> shouldAlter();

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier){
        if(player==null || !player.isOnline()) {
            return "null player";
        }
        Player p = player.getPlayer();
        if(shouldAlter().apply(p) && p != null)
            switch(identifier){
                case "prefix":
                    return getChatColor().apply(p) + getPrefix().apply(p) + " " + p.getName() + ChatColor.RESET + ": ";
                case "color":
                    return getChatColor().apply(p)+ "";
            }
        return "";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mcmepvp";
    }

    @Override
    public @NotNull String getAuthor() {
        return "NicovicTheSixth";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.3";
    }
}
