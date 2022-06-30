package com.mcmiddleearth.mcme.pvpplugin.command.commandParser;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.mcme.pvpplugin.command.PVPCommandSender;
import com.mcmiddleearth.mcme.pvpplugin.util.MapEditor;
import com.mcmiddleearth.mcme.pvpplugin.util.Permissions;
import org.bukkit.entity.Player;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static boolean isState(Player p, MapEditor.EditorState... eState){
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(p.getUniqueId());
        if(me == null)
            return false;
        return Arrays.stream(eState).anyMatch(state -> me.getState() == state);
    }

    public static boolean stageGamemode(McmeCommandSender c) {
        Player source = CommandUtil.getPlayer(c);
        if(source == null)
            return false;
        return isState(source, MapEditor.EditorState.CAPTURETHEFLAG,
                    MapEditor.EditorState.FREEFORALL,
                    MapEditor.EditorState.INFECTED,
                    MapEditor.EditorState.TEAMCONQUEST,
                    MapEditor.EditorState.TEAMDEATHMATCH,
                    MapEditor.EditorState.TEAMSLAYER,
                    MapEditor.EditorState.DEATHRUN);
    }
    public static boolean canEditGoal(McmeCommandSender c) {
        Player source = CommandUtil.getPlayer(c);
        if(source == null)
            return false;
        return isState(source, MapEditor.EditorState.DEATHRUN);
    }

    public static boolean canEditCapture(McmeCommandSender c) {
        Player source = CommandUtil.getPlayer(c);
        if(source == null)
            return false;
        return isState(source,  MapEditor.EditorState.TEAMCONQUEST);
    }
}
