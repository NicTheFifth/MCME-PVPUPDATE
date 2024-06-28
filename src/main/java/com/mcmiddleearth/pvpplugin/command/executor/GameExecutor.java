package com.mcmiddleearth.pvpplugin.command.executor;

import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.*;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.ScoreGoal;
import com.mcmiddleearth.pvpplugin.statics.ArgumentNames;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import com.mojang.brigadier.context.CommandContext;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class GameExecutor {
    /*public static int Action(CommandContext<McmeCommandSender> c){
        Player player = CommandUtil.getPlayer(c.getSource());
        GamemodeRunner runner = PVPPlugin.getInstance().getActiveGame();

        sendBaseComponent(message, player);
        return 1;
    }*/
    //TODO: Finish with above structure
    public static int CreateGame(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        JSONMap map = PVPPlugin.getInstance().getMaps().get(
            c.getArgument(ArgumentNames.MAP_NAME, String.class));
        String gamemode = c.getArgument(ArgumentNames.GAMEMODE, String.class);
        PVPPlugin pvpPlugin = PVPPlugin.getInstance();
        GamemodeRunner runner = null;
        switch(gamemode){
            case(Gamemodes.CAPTURETHEFLAG):
                break;
            case(Gamemodes.DEATHRUN):
                break;
            case(Gamemodes.FREEFORALL):
                break;
            case(Gamemodes.INFECTED):
                runner = new InfectedRunner(map, InfectedRunner.DefaultTimeLimit());
                break;
            case(Gamemodes.ONEINTHEQUIVER):
                runner = new OneInTheQuiverRunner(map, OneInTheQuiverRunner.DefaultScoreGoal());
                break;
            case(Gamemodes.RINGBEARER):
                runner = new RingBearerRunner(map);
                break;
            case(Gamemodes.TEAMCONQUEST):
                runner = new TeamConquestRunner(map, TeamConquestRunner.DefaultScoreGoal());
                break;
            case(Gamemodes.TEAMDEATHMATCH):
                runner = new TeamDeathmatchRunner(map);
                break;
            case(Gamemodes.TEAMSLAYER):
                runner = new TeamSlayerRunner(map,
                    TeamSlayerRunner.DefaultScoreGoal());
        }
        if(runner == null)
            return 0;
        if(pvpPlugin.getActiveGame() == null) {
            pvpPlugin.setActiveGame(runner);
            sendBaseComponent(
                new ComponentBuilder(
                    String.format("Game created: %s on %s",
                        runner.getGamemode(), runner.getMapName()))
                    .color(Style.INFO)
                    .create(),
                player);
            return 1;
        }
        pvpPlugin.getGameQueue().add(runner);
        sendBaseComponent(
            new ComponentBuilder(
                String.format("Game created, added to queue: %s on %s",
                    runner.getGamemode(), runner.getMapName()))
                .color(Style.INFO)
                .create(),
            player);
        return 1;
    }

    public static int StartGame(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        GamemodeRunner runner = PVPPlugin.getInstance().getActiveGame();

        if(runner.canStart(player)) {
            runner.start();
            return 1;
        }
        return 0;
    }

    public static int JoinGame(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        GamemodeRunner runner = PVPPlugin.getInstance().getActiveGame();

        if(runner.canJoin(player)) {
            runner.Join(player);
            return 1;
        }
        return 0;
    }

    public static int SetGoal(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        int scoreGoal = c.getArgument(ArgumentNames.GOAL, Integer.class);

        GamemodeRunner runner = PVPPlugin.getInstance().getActiveGame();

        ((ScoreGoal) runner).setScoreGoal(scoreGoal);
        sendBaseComponent(
            new ComponentBuilder(String.format("Goal set to %d for %s on %s."
                , scoreGoal, runner.getGamemode(), runner.getMapName()))
                .color(Style.INFO).create(),
            player);
        return 1;

    }

    public static int EndGame(CommandContext<McmeCommandSender> c) {
        PVPPlugin pvpPlugin = PVPPlugin.getInstance();
        GamemodeRunner runner = pvpPlugin.getActiveGame();

        runner.end(true);
        pvpPlugin.setActiveGame(pvpPlugin.getGameQueue().poll());
        return 1;
    }
}
