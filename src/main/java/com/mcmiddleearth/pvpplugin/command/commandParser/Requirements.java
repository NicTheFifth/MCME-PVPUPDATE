package com.mcmiddleearth.pvpplugin.command.commandParser;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.pvpplugin.command.PVPCommandSender;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.GamemodeEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.TeamDeathMatchEditor;
import com.mcmiddleearth.pvpplugin.util.Permissions;
import org.bukkit.entity.Player;
import java.util.Arrays;

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
        return Arrays.stream(gamemodes).anyMatch(gamemode -> me.getGamemodeEditor().getClass().getName().equals(gamemode));
    }

    public static boolean stageGamemode(McmeCommandSender c) {
        Player source = CommandUtil.getPlayer(c);
        if(source == null)
            return false;
        return isState(source, //CaptureTheFlag,
                    //FreeForAll,
                    //Infected,
                    //TeamConquest,
                    TeamDeathMatchEditor.class.getName()//,
                    //TeamSlayer,
                    //DeathRun
                );
    }
    public static boolean canEditGoal(McmeCommandSender c) {
        Player source = CommandUtil.getPlayer(c);
        if(source == null)
            return false;
        return isState(source //, MapEditor.EditorState.DEATHRUN
                 );
    }

    public static boolean canEditCapture(McmeCommandSender c) {
        Player source = CommandUtil.getPlayer(c);
        if(source == null)
            return false;
        return isState(source//,  MapEditor.EditorState.TEAMCONQUEST
                 );
    }
}
