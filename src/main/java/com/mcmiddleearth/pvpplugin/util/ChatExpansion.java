package com.mcmiddleearth.pvpplugin.util;

import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
        String placeholderPrefix = "<color><prefix> <name></color>:";
        TagResolver.Single name = Placeholder.parsed("name", player1.getName());
        if(!params.equalsIgnoreCase("prefix"))
            return null;
        if (runner == null || runner.getGameState() == GamemodeRunner.State.QUEUED) {
            if (player1.hasPermission(Permissions.PVP_ADMIN.getPermissionNode()))
                return LegacyComponentSerializer.legacySection().serialize(mm.deserialize(placeholderPrefix,
                        Placeholder.parsed("prefix", "PVP Staff"),
                        Placeholder.styling("color", NamedTextColor.GOLD),
                        name));
            if (player1.hasPermission(Permissions.RUN.getPermissionNode()))
                return LegacyComponentSerializer.legacySection().serialize(mm.deserialize(placeholderPrefix,
                        Placeholder.parsed("prefix", "Manager"),
                        Placeholder.styling("color", NamedTextColor.GOLD),
                        name));
            return LegacyComponentSerializer.legacySection().serialize(mm.deserialize(placeholderPrefix,
                    Placeholder.parsed("prefix", "Lobby"),
                    Placeholder.styling("color", NamedTextColor.GRAY),
                    name));
        }

        if(player2==null || !player2.isOnline())
            return null;
        TagResolver.Single spectatorPrefix = runner.getSpectatorPrefix(player1);
        TagResolver.Single spectatorColor = runner.getSpectatorColor(player1);
        if(spectatorPrefix != null && runner.getSpectatorPrefix(player2) != null)
            return LegacyComponentSerializer.legacySection().serialize(mm.deserialize(placeholderPrefix,
                    spectatorPrefix,
                    spectatorColor,
                    name));
        TagResolver.Single activePlayerPrefix = runner.getPlayerPrefix(player1);
        TagResolver.Single activePlayerColor= runner.getPlayerColor(player1);
        if(activePlayerPrefix != null)
            return LegacyComponentSerializer.legacySection().serialize(mm.deserialize(placeholderPrefix,
                    activePlayerPrefix,
                    activePlayerColor,
                    name));
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
