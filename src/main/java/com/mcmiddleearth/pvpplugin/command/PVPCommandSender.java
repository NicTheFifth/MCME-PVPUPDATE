package com.mcmiddleearth.pvpplugin.command;

import com.mcmiddleearth.command.McmeCommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;

public class PVPCommandSender implements McmeCommandSender {
    CommandSender sender;

    public PVPCommandSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(BaseComponent[] baseComponents) {
        sender.sendMessage(BaseComponent.toLegacyText(baseComponents));
    }

    public static McmeCommandSender wrap(CommandSender sender){return new PVPCommandSender(sender);}

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    @SuppressWarnings("all")
    public CommandSender getSender() {
        return this.sender;
    }
    //</editor-fold>
}
