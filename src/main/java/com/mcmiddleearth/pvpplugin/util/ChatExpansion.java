package com.mcmiddleearth.pvpplugin.util;

import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import com.mcmiddleearth.pvpplugin.PVPPlugin;

public class ChatExpansion extends PlaceholderExpansion {

    PVPPlugin pvpPlugin;
    MiniMessage mm;

    public ChatExpansion(){
        this.pvpPlugin = PVPPlugin.getInstance();
        this.mm = pvpPlugin.getMiniMessage();
    }
    @Override
    public String onRequest(OfflinePlayer p, @NotNull String params) {
        if(p==null || !p.isOnline()) {
            return "null player";
        }
        Player player = p.getPlayer();
        GamemodeRunner runner = pvpPlugin.getActiveGame();
        String placeholderPrefix = "<color><prefix> <name></color>:";
        TagResolver.Single name = Placeholder.parsed("name", player.getName());
        if(!params.equalsIgnoreCase("prefix"))
            return null;
        if (runner == null || runner.getGameState() == GamemodeRunner.State.QUEUED) {
            if (player.hasPermission(Permissions.PVP_ADMIN.getPermissionNode()))
                return LegacyComponentSerializer.legacySection().serialize(mm.deserialize(placeholderPrefix,
                        Placeholder.parsed("prefix", "PVP Staff"),
                        Placeholder.styling("color", NamedTextColor.GOLD),
                        name));
            if (player.hasPermission(Permissions.RUN.getPermissionNode()))
                return LegacyComponentSerializer.legacySection().serialize(mm.deserialize(placeholderPrefix,
                        Placeholder.parsed("prefix", "Manager"),
                        Placeholder.styling("color", NamedTextColor.GOLD),
                        name));
            return LegacyComponentSerializer.legacySection().serialize(mm.deserialize(placeholderPrefix,
                    Placeholder.parsed("prefix", "Lobby"),
                    Placeholder.styling("color", NamedTextColor.GRAY),
                    name));
        }
        TagResolver.Single activePlayerPrefix = runner.getPlayerPrefix(player);
        TagResolver.Single activePlayerColor= runner.getPlayerColor(player);
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