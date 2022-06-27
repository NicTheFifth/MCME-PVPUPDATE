package com.mcmiddleearth.mcme.pvpplugin.command.commandParser;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.command.PVPCommandSender;
import com.mcmiddleearth.mcme.pvpplugin.util.MapEditor;
import com.mcmiddleearth.mcme.pvpplugin.util.Permissions;
import org.bukkit.entity.Player;
import java.util.Arrays;

public class Requirements {
    public static boolean isMapEditor(McmeCommandSender player){
        return ((PVPCommandSender) player).getSender().hasPermission(Permissions.MAP_EDITOR.getPermissionNode());
    }

    public static boolean isAdmin(McmeCommandSender player){
        return ((PVPCommandSender) player).getSender().hasPermission(Permissions.PVP_ADMIN.getPermissionNode());
    }

    public static boolean isState(Player p, MapEditor.EditorState... eState){
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(p.getUniqueId());
        try {
            return Arrays.stream(eState).anyMatch(state -> me.getState() == state);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean stageGamemode(McmeCommandSender c) {
        if(((PVPCommandSender) c).getSender() instanceof Player){
            Player p = (Player) ((PVPCommandSender) c).getSender();
            return isState(p, MapEditor.EditorState.CAPTURETHEFLAG,
                    MapEditor.EditorState.FREEFORALL,
                    MapEditor.EditorState.INFECTED,
                    MapEditor.EditorState.TEAMCONQUEST,
                    MapEditor.EditorState.TEAMDEATHMATCH,
                    MapEditor.EditorState.TEAMSLAYER,
                    MapEditor.EditorState.DEATHRUN);
        }else
            return false;
    }
    public static boolean canEditGoal(McmeCommandSender c) {
        if(((PVPCommandSender) c).getSender() instanceof Player){
            Player p = (Player) ((PVPCommandSender) c).getSender();
            return isState(p, MapEditor.EditorState.DEATHRUN);
        }else
            return false;
    }

    public static boolean canEditCapture(McmeCommandSender c) {
        if(((PVPCommandSender) c).getSender() instanceof Player){
            Player p = (Player) ((PVPCommandSender) c).getSender();
            return isState(p, MapEditor.EditorState.TEAMCONQUEST);
        }else
            return false;
    }

    public static boolean hasRB(McmeCommandSender c) {
        if(((PVPCommandSender) c).getSender() instanceof Player){
            Player p = (Player) ((PVPCommandSender) c).getSender();
            return isState(p, MapEditor.EditorState.CAPTURETHEFLAG,
                    MapEditor.EditorState.FREEFORALL,
                    MapEditor.EditorState.TEAMCONQUEST,
                    MapEditor.EditorState.TEAMDEATHMATCH,
                    MapEditor.EditorState.TEAMSLAYER);
        }else
            return false;
    }

    public static boolean hasIS(McmeCommandSender c) {
        if(((PVPCommandSender) c).getSender() instanceof Player){
            Player p = (Player) ((PVPCommandSender) c).getSender();
            return isState(p, MapEditor.EditorState.INFECTED);
        }else
            return false;
    }

    public static boolean hasDR(McmeCommandSender c) {
        if(((PVPCommandSender) c).getSender() instanceof Player){
            Player p = (Player) ((PVPCommandSender) c).getSender();
            return isState(p, MapEditor.EditorState.DEATHRUN);
        }else
            return false;
    }
}
