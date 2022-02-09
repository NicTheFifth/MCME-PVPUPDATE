package com.mcmiddleearth.mcme.pvpplugin.command.executor;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.util.MapEditor;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.entity.Player;

public class EditExecutor {
    public static int CreateMapEditor(PVPPlugin pvpPlugin, CommandContext<McmeCommandSender> c){
        if(c.getSource() instanceof Player){
            Player source = (Player) c.getSource();
            String mapName = c.getArgument("map", String.class);
            if(!pvpPlugin.getMaps().containsKey(mapName)) {
                JSONMap newMap = new JSONMap(mapName);
                pvpPlugin.getMaps().put(mapName, newMap);
                pvpPlugin.getMapEditors().put(source.getUniqueId(), new MapEditor(newMap));
            }else{
                source.sendMessage("This name is already in use, please use another name" + Style.WARNING);
            }
            return 1;
        }
        return 0;
    }

    public static int NewMapEditor(PVPPlugin pvpPlugin,CommandContext<McmeCommandSender> c){
        if(c.getSource() instanceof Player){
            Player source = (Player) c.getSource();
            String mapName = c.getArgument("map",String.class);
            JSONMap existingMap = pvpPlugin.getMaps().get(mapName);
            pvpPlugin.getMapEditors().put(source.getUniqueId(),new MapEditor(existingMap));
            return 1;
        }
        return 0;
    }

    public static int SetArea(PVPPlugin pvpPlugin,CommandContext<McmeCommandSender> c){
        if(c.getSource() instanceof Player){
            Player source = (Player) c.getSource();
            MapEditor me = pvpPlugin.getMapEditors().get(source.getUniqueId());
            me.setArea(pvpPlugin, source);
            return 1;
        }
        return 0;
    }

    public static int SetTitle(PVPPlugin pvpPlugin, CommandContext<McmeCommandSender> c) {
        if(c.getSource() instanceof Player){
            Player source = (Player) c.getSource();
            String mapName = c.getArgument("map", String.class);
            MapEditor me = pvpPlugin.getMapEditors().get(source.getUniqueId());
            me.setTitle(pvpPlugin, mapName);
            return 1;
        }
        return 0;
    }

    public static int SetRP(PVPPlugin pvpPlugin, CommandContext<McmeCommandSender> c) {
        if(c.getSource() instanceof Player){
            Player source = (Player) c.getSource();
            String rpName = c.getArgument("rp", String.class);
            MapEditor me = pvpPlugin.getMapEditors().get(source.getUniqueId());
            me.setRP(pvpPlugin,rpName);
            return 1;
        }
        return 0;
    }

    public static int SetGamemode(PVPPlugin pvpPlugin, CommandContext<McmeCommandSender> c) {
        if(c.getSource() instanceof Player){
            Player source = (Player) c.getSource();
            String gamemode = c.getArgument("gamemode", String.class);
            MapEditor me = pvpPlugin.getMapEditors().get(source.getUniqueId());
            me.setGamemode(gamemode);
            return 1;
        }
        return 0;
    }

    public static int SetMax(PVPPlugin pvpPlugin, CommandContext<McmeCommandSender> c) {
        //TODO: create SetMax
        return 0;
    }

    public static int EditGoal(PVPPlugin pvpPlugin, CommandContext<McmeCommandSender> c) {
        //TODO:Create EditGoal
        return 0;
    }

    public static int CreateCapture(CommandContext<McmeCommandSender> c, PVPPlugin pvpPlugin) {
        //TODO: Create CreateCapture
        return 0;
    }

    public static int DelCapture(CommandContext<McmeCommandSender> c, PVPPlugin pvpPlugin) {
        //TODO: Create DelCapture
        return 0;
    }

    public static int SetSpawn(CommandContext<McmeCommandSender> c, PVPPlugin pvpPlugin) {
        //TODO:Create SetSpawn
        return 0;
    }
}
