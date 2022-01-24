package com.mcmiddleearth.mcme.pvpplugin.command.commandParser;

import com.google.common.collect.Lists;
import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.util.MapEditor;
import com.mcmiddleearth.mcme.pvpplugin.util.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Requirements {
    public static boolean isMapEditor(McmeCommandSender player){
        return ((CommandSender)player).hasPermission(Permissions.MAP_EDITOR.getPermissionNode());
    }

    public static boolean isAdmin(McmeCommandSender player){
        return ((CommandSender)player).hasPermission(Permissions.PVP_ADMIN.getPermissionNode());
    }

    public static boolean isState(Player p, PVPPlugin pvpPlugin, MapEditor.EditorState... eState){
        MapEditor me = pvpPlugin.getMapEditors().get(p.getUniqueId());
        try {
            return Arrays.stream(eState).anyMatch(state -> me.getState() == state);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean stageGamemode(McmeCommandSender c, PVPPlugin pvpPlugin) {
        if(c instanceof Player){
            Player p = (Player) c;
            return isState(p,pvpPlugin, MapEditor.EditorState.CAPTURETHEFLAG,
                    MapEditor.EditorState.FREEFORALL,
                    MapEditor.EditorState.INFECTED,
                    MapEditor.EditorState.TEAMCONQUEST,
                    MapEditor.EditorState.TEAMDEATHMATCH,
                    MapEditor.EditorState.TEAMSLAYER,
                    MapEditor.EditorState.DEATHRUN);
        }else
            return false;
    }
    public static boolean canEditGoal(McmeCommandSender c, PVPPlugin pvpPlugin) {
        if(c instanceof Player){
            Player p = (Player) c;
            return isState(p,pvpPlugin, MapEditor.EditorState.DEATHRUN);
        }else
            return false;
    }

    public static boolean canEditCapture(McmeCommandSender c, PVPPlugin pvpPlugin) {
        if(c instanceof Player){
            Player p = (Player) c;
            return isState(p,pvpPlugin, MapEditor.EditorState.TEAMCONQUEST);
        }else
            return false;
    }

    public static boolean hasRB(McmeCommandSender c, PVPPlugin pvpPlugin) {
        if(c instanceof Player){
            Player p = (Player) c;
            return isState(p,pvpPlugin, MapEditor.EditorState.CAPTURETHEFLAG,
                    MapEditor.EditorState.FREEFORALL,
                    MapEditor.EditorState.TEAMCONQUEST,
                    MapEditor.EditorState.TEAMDEATHMATCH,
                    MapEditor.EditorState.TEAMSLAYER);
        }else
            return false;
    }

    public static boolean hasIS(McmeCommandSender c, PVPPlugin pvpPlugin) {
        if(c instanceof Player){
            Player p = (Player) c;
            return isState(p,pvpPlugin, MapEditor.EditorState.INFECTED);
        }else
            return false;
    }

    public static boolean hasDR(McmeCommandSender c, PVPPlugin pvpPlugin) {
        if(c instanceof Player){
            Player p = (Player) c;
            return isState(p,pvpPlugin, MapEditor.EditorState.DEATHRUN);
        }else
            return false;
    }
}
