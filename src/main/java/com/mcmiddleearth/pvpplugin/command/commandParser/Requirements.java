package com.mcmiddleearth.pvpplugin.command.commandParser;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.command.PVPCommandSender;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import com.mcmiddleearth.pvpplugin.util.Permissions;
import org.bukkit.entity.Player;
import java.util.Arrays;
import java.util.function.Predicate;

public class Requirements {
    public static boolean canRun(McmeCommandSender player){
        return ((PVPCommandSender) player).getSender().hasPermission(Permissions.RUN.getPermissionNode());
    }
    public static boolean isMapEditor(McmeCommandSender player){
        return ((PVPCommandSender) player).getSender().hasPermission(Permissions.MAP_EDITOR.getPermissionNode());
    }
    public static boolean isAdmin(McmeCommandSender player){
        return ((PVPCommandSender) player).getSender().hasPermission(Permissions.PVP_ADMIN.getPermissionNode());
    }
    public static boolean isState(Player p, String... gamemodes){
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(p.getUniqueId());
        if(me == null)
            return false;
        return Arrays.stream(gamemodes).anyMatch(gamemode -> me.getGamemodeEditor().getGamemode().equals(gamemode));
    }
    public static boolean allGamemode(McmeCommandSender c) {
        Player source = (Player)c;
        if(source == null)
            return false;
        MapEditor me =
            PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null)
            return false;
        return (me.getGamemodeEditor() != null);
    }
    public static boolean canEditGoal(McmeCommandSender c) {
        Player source = (Player)c;
        if(source == null)
            return false;
        return isState(source, Gamemodes.DEATHRUN);
    }
    public static boolean canEditCapture(McmeCommandSender c) {
        Player source = (Player)c;
        if(source == null)
            return false;
        return isState(source, Gamemodes.TEAMCONQUEST);
    }

    public static boolean canEditSpawn(McmeCommandSender c) {
        Player source = (Player)c;
        if(source == null)
            return false;
        return isState(source, Gamemodes.FREEFORALL);
    }
}
