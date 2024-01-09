package com.mcmiddleearth.pvpplugin.command;

import com.mcmiddleearth.command.McmeCommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

public class CommandUtil {

    public static Player getPlayer(McmeCommandSender sender){
       return ((Player)((PVPCommandSender) sender).getSender());
    }
    public static void sendBaseComponent(BaseComponent[] baseComponents,
                                          Player player){
        player.sendMessage(BaseComponent.toLegacyText(baseComponents));
    }
}
