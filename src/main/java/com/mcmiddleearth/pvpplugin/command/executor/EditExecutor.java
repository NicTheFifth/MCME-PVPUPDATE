package com.mcmiddleearth.pvpplugin.command.executor;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.util.MapEditor;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.entity.Player;

public class EditExecutor {

    public static int CreateMap(CommandContext<McmeCommandSender> c){
        Player source = CommandUtil.getPlayer(c.getSource());
        String mapName = c.getArgument("map", String.class);
        if(source == null)
            return 0;
        JSONMap newMap = new JSONMap(mapName);
        PVPPlugin.getInstance().getMaps().put(mapName, newMap);
        PVPPlugin.getInstance().getMapEditors().put(source.getUniqueId(), new MapEditor(newMap));
        source.sendMessage(Style.INFO + String.format("%s created.", mapName));
        return 1;
    }

    public static int NewMapEditor(CommandContext<McmeCommandSender> c){
        Player source = CommandUtil.getPlayer(c.getSource());
        String mapName = c.getArgument("map", String.class);
        if(source == null)
            return 0;
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null){
            JSONMap map = PVPPlugin.getInstance().getMaps().get(mapName);
            PVPPlugin.getInstance().getMapEditors().put(source.getUniqueId(), new MapEditor(map));
        }
        source.sendMessage(String.format("Editor created, ready to edit %s", mapName));
        return 1;
    }

    public static int SetArea(CommandContext<McmeCommandSender> c){
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source == null)
            return 0;
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null){
            source.sendMessage(Style.INFO_LIGHT + "Please select which map you wish to edit with /mapedit <map name>");
            return 0;
        }
        source.sendMessage(me.setArea(source));
        return 1;
    }

    public static int SetTitle(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        String mapName = c.getArgument("map", String.class);
        if(source == null)
            return 0;
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null){
            source.sendMessage(Style.INFO_LIGHT + "Please select which map you wish to edit with /mapedit <map name>");
            return 0;
        }
        source.sendMessage(me.setTitle(mapName));
        return 1;
    }

    public static int SetRP(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        String rpName = c.getArgument("rp", String.class);
        if(source == null)
            return 0;
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null){
            source.sendMessage(Style.INFO_LIGHT + "Please select which map you wish to edit with /mapedit <map name>");
            return 0;
        }
        source.sendMessage(me.setRP(rpName));
        return 1;
    }

    public static int SetGamemode(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        String gamemode = c.getArgument("gamemode", String.class);
        if(source == null)
            return 0;
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null){
            source.sendMessage(Style.INFO_LIGHT + "Please select which map you wish to edit with /mapedit <map name>");
            return 0;
        }
        source.sendMessage(me.setGamemode(gamemode));
        return 1;
    }

    public static int SetMax(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        Integer max = c.getArgument("amount", Integer.class);
        if(source == null)
            return 0;
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null){
            source.sendMessage(Style.INFO_LIGHT + "Please select which map you wish to edit with /mapedit <map name>");
            return 0;
        }
        source.sendMessage(me.setMax(max));
        return 1;
    }

    public static int EditGoal(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source == null)
            return 0;
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null){
            source.sendMessage(Style.INFO_LIGHT + "Please select which map you wish to edit with /mapedit <map name>");
            return 0;
        }
        source.sendMessage(me.setGoal(source));
        return 1;
    }

    public static int CreateCapture(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source == null)
            return 0;
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null){
            source.sendMessage(Style.INFO_LIGHT + "Please select which map you wish to edit with /mapedit <map name>");
            return 0;
        }
        source.sendMessage(me.createCapturePoint(source));
        return 1;
    }

    public static int DelCapture(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        Integer point = c.getArgument("pointNum", Integer.class);
        if(source == null)
            return 0;
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null){
            source.sendMessage(Style.INFO_LIGHT + "Please select which map you wish to edit with /mapedit <map name>");
            return 0;
        }
        source.sendMessage(me.delCapturePoint(point));
        return 1;
    }
    public static int setMapSpawn(CommandContext<McmeCommandSender> c){
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source == null)
            return 0;
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null){
            source.sendMessage(Style.INFO_LIGHT + "Please select which map you wish to edit with /mapedit <map name>");
            return 0;
        }
        source.sendMessage(me.setSpawn(source.getLocation()));
        return 1;

    }

    public static int setSpawn(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        String spawnType = c.getArgument("spawn", String.class);
        if(source == null)
            return 0;
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null){
            source.sendMessage(Style.INFO_LIGHT + "Please select which map you wish to edit with /mapedit <map name>");
            return 0;
        }
        String[] response;
        switch(spawnType){
            case "red":
                response = me.setRedSpawn(source.getLocation());
                break;
            case "blue":
                response = me.setBlueSpawn(source.getLocation());
                break;
            case"death":
                response = me.setDeathSpawn(source.getLocation());
                break;
            case "runner":
                response = me.setRunnerSpawn(source.getLocation());
                break;
            case "infected":
                response = me.setInfectedSpawn(source.getLocation());
                break;
            case "survivor":
                response = me.setSurvivorSpawn(source.getLocation());
                break;
            default:
                source.sendMessage();
                return 0;
        }
        source.sendMessage(response);
        return 1;
    }
}
