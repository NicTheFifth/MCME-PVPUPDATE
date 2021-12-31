package com.mcmiddleearth.mcme.pvpplugin.command.commandParser;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.mcme.pvpplugin.util.Permissions;
import org.bukkit.command.CommandSender;

public class Requirements {
    public static boolean isMapEditor(McmeCommandSender player){
        return ((CommandSender)player).hasPermission(Permissions.MAP_EDITOR.getPermissionNode());
    }

    public static boolean isAdmin(McmeCommandSender player){
        return ((CommandSender)player).hasPermission(Permissions.PVP_ADMIN.getPermissionNode());
    }
}
