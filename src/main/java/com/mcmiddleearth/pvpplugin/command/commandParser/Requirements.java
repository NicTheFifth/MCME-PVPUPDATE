package com.mcmiddleearth.pvpplugin.command.commandParser;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.pvpplugin.command.PVPCommandSender;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.*;
import com.mcmiddleearth.pvpplugin.util.Permissions;
import org.bukkit.entity.Player;

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
    public static <T> boolean isGamemodeEditorOf(Class<T> checkedClass,
                                               McmeCommandSender c){
        Player source = CommandUtil.getPlayer(c);
        MapEditor me =
            PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null)
            return false;
        if(!GamemodeEditor.class.isAssignableFrom(checkedClass))
            return false;
        if(me.getGamemodeEditor() == null)
            return false;
        return checkedClass.isAssignableFrom(me.getGamemodeEditor().getClass());
    }

    public static boolean hasActiveMapEditor(McmeCommandSender c) {
        Player source = CommandUtil.getPlayer(c);
        MapEditor me =
            PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        return me != null;
    }
    public static boolean hasActiveGamemodeEditor(McmeCommandSender c) {
        Player source = CommandUtil.getPlayer(c);
        MapEditor me =
            PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null)
            return false;
        return me.getGamemodeEditor() != null;
    }

    public static boolean instanceOfSpawnListEditor(McmeCommandSender c) {
        Player source = CommandUtil.getPlayer(c);
        MapEditor me =
            PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        return me.getGamemodeEditor() instanceof SpawnListEditor;
    }

    public static boolean NotInstanceOfSpawnListEditor(McmeCommandSender c) {
        return !instanceOfSpawnListEditor(c);
    }

    public static boolean InstanceOfTeamSpawnEditor(McmeCommandSender c) {
        Player source = CommandUtil.getPlayer(c);
        MapEditor me =
            PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        return me.getGamemodeEditor() instanceof TeamSpawnEditor;
    }

    public static boolean InstanceOfTeamSpawnListEditor(McmeCommandSender c) {
        Player source = CommandUtil.getPlayer(c);
        MapEditor me =
            PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        return me.getGamemodeEditor() instanceof TeamSpawnListEditor;
    }

    public static boolean hasSpecialPoints(McmeCommandSender c) {
        Player source = CommandUtil.getPlayer(c);
        MapEditor me =
            PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null)
            return false;
        if(me.getGamemodeEditor() == null)
            return false;
        return me.getGamemodeEditor() instanceof SpecialPointListEditor ||
            me.getGamemodeEditor() instanceof SpecialPointEditor;

    }

    public static boolean isInstanceOfSpecialPointEditor(McmeCommandSender c) {
        Player source = CommandUtil.getPlayer(c);
        MapEditor me =
            PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        return me.getGamemodeEditor() instanceof SpecialPointEditor;
    }

    public static boolean isInstanceOfSpecialPointListEditor(McmeCommandSender c) {
        Player source = CommandUtil.getPlayer(c);
        MapEditor me =
            PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        return me.getGamemodeEditor() instanceof SpecialPointListEditor;
    }
}
