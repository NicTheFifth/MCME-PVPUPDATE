package com.mcmiddleearth.mcme.pvpplugin.command.executor;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.jsonGamemodes.JSONGamemode;
import com.mcmiddleearth.mcme.pvpplugin.runners.GamemodeRunner;
import com.mcmiddleearth.mcme.pvpplugin.util.GameCreator;
import com.mcmiddleearth.mcme.pvpplugin.util.Gamemodes;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GameExecutor {
    /*
            public static int Action(CommandContext<McmeCommandSender> c){
                Player source = CommandUtil.getPlayer(c.getSource());
                if(source == null)
                    return 0;
                source.sendMessage(me.Action(mapName));
                return 1;
            }
             */
    //TODO: Finish with above structure
    public static int getRule(CommandContext<McmeCommandSender> c){
        Player source = CommandUtil.getPlayer(c.getSource());
        String gamemode = c.getArgument("gamemode", String.class);
        if(source == null)
            return 0;
        source.sendMessage(Gamemodes.getRules(gamemode));
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

    public static int listMaps(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source == null)
            return 0;
        PVPPlugin.getInstance().getMaps().values().forEach(m -> GameExecutor.printMap(source, m));
        return 1;
    }

    private static void printMap(Player source, JSONMap jsonMap) {
        source.sendMessage(ChatColor.GREEN + jsonMap.getTitle());
        if(jsonMap.getJSONCaptureTheFlag() != null)
            source.sendMessage(Style.INFO + "    Capture the Flag");
        if(jsonMap.getJSONDeathRun() != null)
            source.sendMessage(Style.INFO + "    Death Run");
        if(jsonMap.getJSONFreeForAll() != null)
            source.sendMessage(Style.INFO + "    Free for All");
        if(jsonMap.getJSONInfected() != null)
            source.sendMessage(Style.INFO + "    Infected");
        if(jsonMap.getJSONTeamConquest() != null)
            source.sendMessage(Style.INFO + "    Team Conquest");
        if(jsonMap.getJSONTeamDeathMatch() != null)
            source.sendMessage(Style.INFO + "    Team Deathmatch");
        if(jsonMap.getJSONTeamSlayer() != null)
            source.sendMessage(Style.INFO + "    TeamSlayer");
    }

    public static int createGame(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        String map = null, gamemode = null;
        Integer var = null;
        if(source == null)
            return 0;
        try {
            map = c.getArgument("map", String.class);
        } catch(Exception ignored){}
        try {
            gamemode = c.getArgument("gamemode", String.class);
        } catch(Exception ignored){}
        try{
            var = c.getArgument("var", Integer.class);
        }catch(Exception ignored){}

        GameCreator newCreator = new GameCreator();
        boolean creatorMade = false;
        String extraMessage = null;

        if(var != null){
            newCreator = new GameCreator(map,gamemode,var);
            creatorMade = true;
        }
        if(gamemode != null && !creatorMade){
            newCreator = new GameCreator(map,gamemode);
            creatorMade = true;
        }
        if(map != null && !creatorMade){
            newCreator = new GameCreator(map);
            extraMessage = Style.INFO_STRESSED + "Please set a gamemode too with /game load gamemode <gamemode>";
            creatorMade = true;
        }
        if(!creatorMade){
            extraMessage = Style.INFO_STRESSED + "Please set a gamemode and map with /game load map <mapName> /game load gamemode <gamemode>";
        }

        PVPPlugin.getInstance().getGameCreators().put(source.getUniqueId(), newCreator);
        source.sendMessage(Style.INFO + "Game creator made, when ready to start, type /game load private or /game load public");
        if(extraMessage != null)
            source.sendMessage(extraMessage);
        return 1;
    }
}
