package com.mcmiddleearth.pvpplugin.util;

import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import com.mcmiddleearth.pvpplugin.PVPPlugin;

public class ChatExpansion extends PlaceholderExpansion implements Relational {

    PVPPlugin pvpPlugin;
    MiniMessage mm;

    public ChatExpansion(){
        this.pvpPlugin = PVPPlugin.getInstance();
        this.mm = pvpPlugin.getMiniMessage();
    }
    @Override
    public String onPlaceholderRequest(Player player1, Player player2, String params) {
        if(player1==null || !player1.isOnline())
            return null;
        GamemodeRunner runner = pvpPlugin.getActiveGame();
        String placeholderPrefix = "<prefix> <name><reset>:";
        String placeholderColor = "<color>";
        TagResolver.Single name = Placeholder.parsed("name", player1.getName());
        if(runner == null) {
            switch (params) {
                case "prefix":
                    if (pvpPlugin.getActiveGame() == null) {
                        if (player1.hasPermission(Permissions.PVP_ADMIN.getPermissionNode()))
                            return mm.serialize(mm.deserialize(placeholderPrefix,
                                    Placeholder.parsed("prefix", "PVP Staff"),
                                    name));
                        if (player1.hasPermission(Permissions.RUN.getPermissionNode()))
                            return mm.serialize(mm.deserialize(placeholderPrefix,
                                    Placeholder.parsed("prefix", "Manager"),
                                    name));
                        return mm.serialize(mm.deserialize(placeholderPrefix,
                                Placeholder.parsed("prefix", "Lobby"),
                                name));
                    }
                    break;
                case "color":
                    if (pvpPlugin.getActiveGame() == null) {
                        if (player1.hasPermission(Permissions.RUN.getPermissionNode()))
                            return mm.serialize(mm.deserialize(placeholderColor,
                                    Placeholder.styling("color", NamedTextColor.GOLD)));
                        return mm.serialize(mm.deserialize(placeholderColor,
                                Placeholder.styling("color", NamedTextColor.GRAY)));
                    }
            }

            return null;
        }
        if(player2==null || !player2.isOnline())
            return null;
        switch(params){
            case "prefix":
                TagResolver.Single spectatorPrefix = runner.getSpectatorPrefix(player1);
                if(spectatorPrefix != null && runner.getSpectatorPrefix(player2) != null)
                    return mm.serialize(mm.deserialize(placeholderPrefix,
                            spectatorPrefix,
                            name));
                TagResolver.Single activePlayerPrefix = runner.getPlayerPrefix(player1);
                if(activePlayerPrefix != null)
                    return mm.serialize(mm.deserialize(placeholderPrefix,
                            activePlayerPrefix,
                            name));
                break;
            case "color":
                TagResolver.Single spectatorColor = runner.getSpectatorColor(player1);
                if (spectatorColor != null && runner.getSpectatorColor(player2) != null)
                    return mm.serialize(mm.deserialize(placeholderColor,
                            spectatorColor));
                TagResolver.Single activePlayerColor = runner.getPlayerColor(player1);
                if (activePlayerColor != null)
                    return mm.serialize(mm.deserialize(placeholderColor,
                            activePlayerColor));
        }
        return null;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "PVP-Plugin";
    }

    @Override
    public @NotNull String getAuthor() {
        return "NicovicTheSixth";
    }

    @Override
    public @NotNull String getVersion() {
        return pvpPlugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }
}
