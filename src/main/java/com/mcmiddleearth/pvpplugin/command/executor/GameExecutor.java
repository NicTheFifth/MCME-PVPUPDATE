package com.mcmiddleearth.pvpplugin.command.executor;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.pvpplugin.exceptions.UnloadableGamemodeException;
import com.mcmiddleearth.pvpplugin.exceptions.UnloadableMapException;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.runners.GamemodeRunner;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GameExecutor {
            /*public static int Action(CommandContext<McmeCommandSender> c){
                Player source = CommandUtil.getPlayer(c.getSource());
                if(source == null)
                    return 0;
                source.sendMessage(me.Action(mapName));
                return 1;
            }*/


    //TODO: Finish with above structure
    public static int getRule(CommandContext<McmeCommandSender> c){
        Player source = CommandUtil.getPlayer(c.getSource());
        String gamemode = c.getArgument("gamemode", String.class);
        if(source == null)
            return 0;
        //source.sendMessage(Gamemodes.getRules(gamemode));
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

        //GameCreator newCreator = new GameCreator();
        boolean creatorMade = false;
        String extraMessage = null;
        try {
            if (var != null) {
                //newCreator = new GameCreator(map, gamemode, var);
                creatorMade = true;
            }
            if (gamemode != null && !creatorMade) {
                //newCreator = new GameCreator(map, gamemode);
                creatorMade = true;
            }
            if (map != null && !creatorMade) {
                //newCreator = new GameCreator(map);
                extraMessage = Style.INFO_STRESSED + "Please set a gamemode too with /pvp load gamemode <gamemode>";
                creatorMade = true;
            }
            if (!creatorMade) {
                extraMessage = Style.INFO_STRESSED + "Please set a gamemode and map with /pvp load map <mapName> and /pvp load gamemode <gamemode>";
            }

            //PVPPlugin.getInstance().getGameCreators().put(source.getUniqueId(), newCreator);
            source.sendMessage(Style.INFO + "Game creator made, when ready to start, type /pvp load private or /pvp load public");
            if (extraMessage != null)
                source.sendMessage(extraMessage);
            return 1;
        } catch(UnloadableMapException | UnloadableGamemodeException e){
            if (e instanceof UnloadableMapException){
                source.sendMessage(Style.ERROR + "This map is not configured yet, please configure it or pick another map");
                return 0;
            } else
                source.sendMessage(Style.ERROR + "This gamemode is not configured yet, please configure it or pick another gamemode");
                return 0;
        }
    }

    public static int loadPublic(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        GamemodeRunner runner = PVPPlugin.getInstance().getActiveGame();
        if(source == null)
            return 0;
        if(runner != null) {
            source.sendMessage(Style.ERROR + "A game is running, please load again later.");
            return 0;
        }
        //GameCreator creator = PVPPlugin.getInstance().getGameCreators().get(source.getUniqueId());
        //if(creator == null){
            source.sendMessage(Style.ERROR + "You have no creator, thus can't load a game.");
            return 0;
        //}
        //source.sendMessage(creator.loadPublic());
        //return 1;
    }

    public static int loadPrivate(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        GamemodeRunner runner = PVPPlugin.getInstance().getActiveGame();
        if(source == null)
            return 0;
        if(runner != null) {
            source.sendMessage(Style.ERROR + "A game is running, please load again later.");
            return 0;
        }
        //GameCreator creator = PVPPlugin.getInstance().getGameCreators().get(source.getUniqueId());
        //if(creator == null){
            source.sendMessage(Style.ERROR + "You have no creator, thus can't load a game.");
            return 0;
        //}
        //source.sendMessage(creator.loadPrivate());
        //return 1;
    }

    public static int loadMap(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        JSONMap map = PVPPlugin.getInstance().getMaps().get(c.getArgument("map", String.class));
        GamemodeRunner runner = PVPPlugin.getInstance().getActiveGame();
        if(source == null)
            return 0;
        if(runner != null) {
            source.sendMessage(Style.ERROR + "A game is running, please load again later.");
            return 0;
        }
        if(map == null){
            source.sendMessage(Style.ERROR + "This map does not exist.");
            return 0;
        }
        //GameCreator creator = PVPPlugin.getInstance().getGameCreators().get(source.getUniqueId());
        //if(creator == null){
            source.sendMessage(Style.ERROR + "You have no creator, thus can't load a map.");
            return 0;
        //}
        //source.sendMessage(creator.loadMap(map));
        //return 1;
    }

    public static int loadGamemode(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        String gamemode = c.getArgument("gamemode", String.class);
        GamemodeRunner runner = PVPPlugin.getInstance().getActiveGame();
        if(source == null)
            return 0;
        if(runner != null) {
            source.sendMessage(Style.ERROR + "A game is running, please load again later.");
            return 0;
        }
        //GameCreator creator = PVPPlugin.getInstance().getGameCreators().get(source.getUniqueId());
        //if(creator == null){
            source.sendMessage(Style.ERROR + "You have no creator, thus can't load a map.");
            return 0;
        //}
        //source.sendMessage(creator.loadGamemode(gamemode));
        //return 1;
    }

    public static int startGame(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source == null)
            return 0;
        if(PVPPlugin.getInstance().getActiveGame() == null){
            source.sendMessage(Style.ERROR + "Cannot start a game, if no game is running.");
            return 0;
        }
        if(PVPPlugin.getInstance().getActiveGame().canStart()){
            PVPPlugin.getInstance().getActiveGame().start();
            source.sendMessage(Style.INFO + "Game started!");
            return 1;
        }
        source.sendMessage(Style.ERROR + "Game cannot start, do you have enough players?");
        return 0;
    }

    public static int endGame(CommandContext<McmeCommandSender> c) {
        Player source = CommandUtil.getPlayer(c.getSource());
        if(source == null)
            return 0;
        if(PVPPlugin.getInstance().getActiveGame() == null){
            source.sendMessage(Style.ERROR + "Cannot end a game, if no game is running.");
            return 0;
        }
        PVPPlugin.getInstance().getActiveGame().end(true);
        source.sendMessage(Style.INFO + "Ended the game!");
        return 1;
    }
}
