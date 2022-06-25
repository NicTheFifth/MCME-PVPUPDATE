package com.mcmiddleearth.mcme.pvpplugin.command;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PVPCommandSender implements McmeCommandSender {
    private final PVPPlugin pvpPlugin;
    CommandSender sender;

    public PVPCommandSender(CommandSender sender, PVPPlugin pvpPlugin) {
        this.sender = sender;
        this.pvpPlugin = pvpPlugin;
    }

    @Override
    public void sendMessage(BaseComponent[] baseComponents) {
        sender.sendMessage(BaseComponent.toLegacyText(baseComponents));
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    @SuppressWarnings("all")
    public CommandSender getSender() {
        return this.sender;
    }
    //</editor-fold>
}
