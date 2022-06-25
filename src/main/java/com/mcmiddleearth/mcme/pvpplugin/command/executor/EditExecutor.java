package com.mcmiddleearth.mcme.pvpplugin.command.executor;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.util.MapEditor;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.entity.Player;

public class EditExecutor {
    public static int CreateMapEditor(CommandContext<McmeCommandSender> c){
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source != null){
            String mapName = c.getArgument("map", String.class);
            if(!PVPPlugin.getInstance().getMaps().containsKey(mapName)) {
                JSONMap newMap = new JSONMap(mapName);
                PVPPlugin.getInstance().getMaps().put(mapName, newMap);
                PVPPlugin.getInstance().getMapEditors().put(source.getUniqueId(), new MapEditor(newMap));
            }else{
                source.sendMessage("This name is already in use, please use another name" + Style.WARNING);
            }
            return 1;
        }
        return 0;
    }

    public static int NewMapEditor(CommandContext<McmeCommandSender> c){
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source != null){
            String mapName = c.getArgument("map",String.class);
            JSONMap existingMap = PVPPlugin.getInstance().getMaps().get(mapName);
            PVPPlugin.getInstance().getMapEditors().put(source.getUniqueId(),new MapEditor(existingMap));
            return 1;
        }
        return 0;
    }

    public static int SetArea(CommandContext<McmeCommandSender> c){
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source != null){
            MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
            me.setArea(PVPPlugin.getInstance(), source);
            return 1;
        }
        return 0;
    }

    public static int SetTitle(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source != null){
            String mapName = c.getArgument("map", String.class);
            MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
            me.setTitle(PVPPlugin.getInstance(), mapName);
            return 1;
        }
        return 0;
    }

    public static int SetRP(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source != null){
            String rpName = c.getArgument("rp", String.class);
            MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
            me.setRP(rpName);
            return 1;
        }
        return 0;
    }

    public static int SetGamemode(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source != null){
            String gamemode = c.getArgument("gamemode", String.class);
            MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
            me.setGamemode(gamemode);
            return 1;
        }
        return 0;
    }

    public static int SetMax(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source != null){
            Integer max = c.getArgument("amount", Integer.class);
            MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
            me.setMax(max);
            return 1;
        }
        return 0;
    }

    public static int EditGoal(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source != null){
            MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
            me.setGoal(source);
            return 1;
        }
        return 0;
    }

    public static int CreateCapture(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source != null){
            MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
            me.createCapturePoint(source);
            return 1;
        }
        return 0;
    }

    public static int DelCapture(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source != null){
            Integer point = c.getArgument("pointNum", Integer.class);
            MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
            me.delCapturePoint(point);
            return 1;
        }
        return 0;
    }

    public static int SetSpawn(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source != null){
            String spawnType = c.getArgument("spawn", String.class);
            MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
            switch(spawnType){
                case "red":
                    me.setRedSpawn(source.getLocation());
                    break;
                case "blue":
                    me.setBlueSpawn(source.getLocation());
                    break;
                case"death":
                    me.setDeathSpawn(source.getLocation());
                    break;
                case "runner":
                    me.setRunnerSpawn(source.getLocation());
                    break;
                case "infected":
                    me.setInfectedSpawn(source.getLocation());
                    break;
                case "survivor":
                    me.setSurvivorSpawn(source.getLocation());
                    break;
                default: return 0;
            }
            return 1;
        }
        return 0;
    }
}
