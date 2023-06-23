package com.mcmiddleearth.pvpplugin.command;

import com.mcmiddleearth.command.McmeCommandSender;
import org.bukkit.entity.Player;

public class CommandUtil {

    public static Player getPlayer(McmeCommandSender sender){
        if(!(sender instanceof PVPCommandSender))
            return null;
        if(!(((PVPCommandSender) sender).getSender() instanceof Player))
            return null;
        return (Player) ((PVPCommandSender) sender).getSender();
    }
}
