package com.mcmiddleearth.mcme.pvpplugin.command.executor;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.mcme.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.mcme.pvpplugin.util.Gamemodes;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.entity.Player;

public class GameExecutor {
    /*
            public static int Action(CommandContext<McmeCommandSender> c){
                Player source = CommandUtil.getPlayer(c.getSource());
                String mapName = c.getArgument("map", String.class);
                if(source == null)
                    return 0;
                MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
                if(me == null){
                    source.sendMessage(Style.INFO_LIGHT + "Please select which map you wish to edit with /mapedit <map name>");
                    return 0;
                    OR
                    if(createMapEditor(source, mapName))
                        return Action(c);
                    return 0;
                }
                source.sendMessage(me.Action(mapName));
                return 1;
            }
             */
    //TODO: Refactor with above structure
    public static int GetRule(CommandContext<McmeCommandSender> c){
        Player source = CommandUtil.getPlayer(c.getSource());
        String gamemode = c.getArgument("gamemode", String.class);
        if(source == null)
            return 0;
        source.sendMessage(Gamemodes.GetRules(gamemode));
        return 1;
    }
}
