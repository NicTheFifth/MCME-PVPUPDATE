package com.mcmiddleearth.mcme.pvpplugin.command.executor;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.mcme.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.mcme.pvpplugin.util.Gamemodes;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.entity.Player;

public class GameExecutor {
    public static int GetRule(CommandContext<McmeCommandSender> c){
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source != null){
            source.sendMessage(Gamemodes.GetRules(c.getArgument("gamemode", String.class)));
            return 1;
        }
        return 0;
    }
}
