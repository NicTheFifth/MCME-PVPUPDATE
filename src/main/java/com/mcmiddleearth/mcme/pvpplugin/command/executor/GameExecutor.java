package com.mcmiddleearth.mcme.pvpplugin.command.executor;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.mcme.pvpplugin.runners.GamemodeRunner;
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
                source.sendMessage(me.Action(mapName));
                return 1;
            }
             */
    //TODO: Refactor with above structure
    public static int getRule(CommandContext<McmeCommandSender> c){
        Player source = CommandUtil.getPlayer(c.getSource());
        String gamemode = c.getArgument("gamemode", String.class);
        if(source == null)
            return 0;
        source.sendMessage(Gamemodes.GetRules(gamemode));
        return 1;
    }

    public static int joinGame(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        GamemodeRunner runner = PVPPlugin.getInstance().getActiveGame();
        if(source == null)
            return 0;
        if(runner == null) {
            source.sendMessage(Style.ERROR + "No game is running, please join again later.");
            return 0;
        }
        runner.tryJoin(source).forEach(source::sendMessage);
        return 1;
    }
}
