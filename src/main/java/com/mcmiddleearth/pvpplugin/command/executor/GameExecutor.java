package com.mcmiddleearth.pvpplugin.command.executor;

import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.*;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.ScoreGoal;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.TimeLimit;
import com.mcmiddleearth.pvpplugin.statics.ArgumentNames;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import com.mojang.brigadier.context.CommandContext;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

import java.util.Objects;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class GameExecutor {
    /*public static int Action(CommandContext<McmeCommandSender> c){
        Player player = CommandUtil.getPlayer(c.getSource());
        GamemodeRunner runner = PVPPlugin.getInstance().getActiveGame();

        sendBaseComponent(message, player);
        return 1;
    }*/
    //TODO: Finish with above structure
    public static int ToggleAutojoin(CommandContext<McmeCommandSender> c) {
        PVPPlugin pvpPlugin = PVPPlugin.getInstance();
        Player player = CommandUtil.getPlayer(c.getSource());
        String text = "you won't automatically join games anymore.";
        if(!pvpPlugin.getAutojoiners().remove(player)) {
            pvpPlugin.getAutojoiners().add(player);
            text = "you'll automatically join games now.";
        }
        sendBaseComponent(
                new ComponentBuilder("Toggled autojoin, " + text).color(Style.INFO).create(),
                player
        );
        return 1;
    }

    public static int CreateGame(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        JSONMap map = PVPPlugin.getInstance().getMaps().get(
            c.getArgument(ArgumentNames.MAP_NAME, String.class));
        String gamemode = c.getArgument(ArgumentNames.GAMEMODE, String.class);
        PVPPlugin pvpPlugin = PVPPlugin.getInstance();
        GamemodeRunner runner = null;
        switch(gamemode){
            case(Gamemodes.CAPTURETHEFLAG):
                runner = new CaptureTheFlagRunner(map, CaptureTheFlagRunner.GetDefaultScoreGoal(), CaptureTheFlagRunner.GetDefaultTimeLimit());
                break;
            case(Gamemodes.DEATHRUN):
                runner = new DeathRunRunner(map, DeathRunRunner.DefaultTimeLimit());
                break;
            case(Gamemodes.FREEFORALL):
                runner = new FreeForAllRunner(map, FreeForAllRunner.DefaultTimeLimit());
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
            pvpPlugin.getAutojoiners().forEach(runner::Join);
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

    public static int CreateGameWithTimeLimit(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        JSONMap map = PVPPlugin.getInstance().getMaps().get(
                c.getArgument(ArgumentNames.MAP_NAME, String.class));
        String gamemode = c.getArgument(ArgumentNames.GAMEMODE, String.class);
        Integer timeLimit = c.getArgument(ArgumentNames.TIME_LIMIT, Integer.class);
        PVPPlugin pvpPlugin = PVPPlugin.getInstance();
        GamemodeRunner runner = null;
        switch(gamemode) {
            case(Gamemodes.FREEFORALL):
                runner = new FreeForAllRunner(map, timeLimit);
                break;
            case (Gamemodes.CAPTURETHEFLAG):
                runner = new CaptureTheFlagRunner(map, CaptureTheFlagRunner.GetDefaultScoreGoal(), timeLimit);
                break;
            case (Gamemodes.DEATHRUN):
                runner = new DeathRunRunner(map, timeLimit);
                break;
            case (Gamemodes.INFECTED):
                runner = new InfectedRunner(map, timeLimit);
                break;
        }
        if(runner == null)
            return 0;
        if(pvpPlugin.getActiveGame() == null) {
            pvpPlugin.setActiveGame(runner);
            sendBaseComponent(
                    new ComponentBuilder(
                            String.format("Game created: %s on %s with time limit %d",
                                    runner.getGamemode(), runner.getMapName(), timeLimit))
                            .color(Style.INFO)
                            .create(),
                    player);
            pvpPlugin.getAutojoiners().forEach(runner::Join);
            return 1;
        }
        pvpPlugin.getGameQueue().add(runner);
        sendBaseComponent(
                new ComponentBuilder(
                        String.format("Game created, added to queue: %s on %s with time limit %d",
                                runner.getGamemode(), runner.getMapName(), timeLimit))
                        .color(Style.INFO)
                        .create(),
                player);
        return 1;
    }

    public static int CreateGameWithScoreGoal(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        JSONMap map = PVPPlugin.getInstance().getMaps().get(
                c.getArgument(ArgumentNames.MAP_NAME, String.class));
        String gamemode = c.getArgument(ArgumentNames.GAMEMODE, String.class);
        Integer scoreGoal = c.getArgument(ArgumentNames.SCORE_GOAL, Integer.class);
        PVPPlugin pvpPlugin = PVPPlugin.getInstance();
        GamemodeRunner runner = null;
        switch(gamemode){
            case(Gamemodes.CAPTURETHEFLAG):
                runner = new CaptureTheFlagRunner(map, scoreGoal, CaptureTheFlagRunner.GetDefaultTimeLimit());
                break;
            case(Gamemodes.ONEINTHEQUIVER):
                runner = new OneInTheQuiverRunner(map, scoreGoal);
                break;
            case(Gamemodes.TEAMCONQUEST):
                runner = new TeamConquestRunner(map, scoreGoal);
                break;
            case(Gamemodes.TEAMSLAYER):
                runner = new TeamSlayerRunner(map, scoreGoal);
        }
        if(runner == null)
            return 0;
        if(pvpPlugin.getActiveGame() == null) {
            pvpPlugin.setActiveGame(runner);
            sendBaseComponent(
                    new ComponentBuilder(
                            String.format("Game created: %s on %s with score goal %d",
                                    runner.getGamemode(), runner.getMapName(), scoreGoal))
                            .color(Style.INFO)
                            .create(),
                    player);
            pvpPlugin.getAutojoiners().forEach(runner::Join);
            return 1;
        }
        pvpPlugin.getGameQueue().add(runner);
        sendBaseComponent(
                new ComponentBuilder(
                        String.format("Game created: %s on %s with score goal %d",
                                runner.getGamemode(), runner.getMapName(), scoreGoal))
                        .color(Style.INFO)
                        .create(),
                player);
        return 1;
    }

    public static int CreateGameWithTimeLimitAndScoreGoal(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        JSONMap map = PVPPlugin.getInstance().getMaps().get(
                c.getArgument(ArgumentNames.MAP_NAME, String.class));
        String gamemode = c.getArgument(ArgumentNames.GAMEMODE, String.class);
        Integer timeLimit = c.getArgument(ArgumentNames.TIME_LIMIT, Integer.class);
        Integer scoreGoal = c.getArgument(ArgumentNames.SCORE_GOAL, Integer.class);
        PVPPlugin pvpPlugin = PVPPlugin.getInstance();
        GamemodeRunner runner;
        if(Objects.equals(gamemode, Gamemodes.CAPTURETHEFLAG))
            runner = new CaptureTheFlagRunner(map, scoreGoal, timeLimit);
        else
            return 0;
        if(pvpPlugin.getActiveGame() == null) {
            pvpPlugin.setActiveGame(runner);
            sendBaseComponent(
                    new ComponentBuilder(
                            String.format("Game created: %s on %s with time limit %d and score goal %d",
                                    runner.getGamemode(), runner.getMapName(), timeLimit, scoreGoal))
                            .color(Style.INFO)
                            .create(),
                    player);
            pvpPlugin.getAutojoiners().forEach(runner::Join);
            return 1;
        }
        pvpPlugin.getGameQueue().add(runner);
        sendBaseComponent(
                new ComponentBuilder(
                        String.format("Game created: %s on %s with time limit %d and score goal %d",
                                runner.getGamemode(), runner.getMapName(), timeLimit, scoreGoal))
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

    public static int LeaveGame(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        GamemodeRunner runner = PVPPlugin.getInstance().getActiveGame();

        if(runner.canJoin(player)) {
            runner.leaveGame(player, false);
            return 1;
        }
        return 0;
    }

    public static int SendRules(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        String gamemode = c.getArgument(ArgumentNames.GAMEMODE, String.class);
        BaseComponent[] message = Gamemodes.getRules.get(gamemode);
        if(message == null){
            sendBaseComponent(new ComponentBuilder(String.format("Please report in dev-public that %s has no rules set!", gamemode)).color(Style.ERROR).create(),
                    player);
            return 0;
        }
        sendBaseComponent(message, player);
        return 1;
    }

    public static int SendActiveGamemodeRules(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        GamemodeRunner gamemodeRunner = PVPPlugin.getInstance().getActiveGame();
        if(gamemodeRunner == null){
            sendBaseComponent(new ComponentBuilder("There is no active game running.").color(Style.ERROR).create(),
                    player);
            return 0;
        }
        String gamemode = gamemodeRunner.getGamemode();
        BaseComponent[] message = Gamemodes.getRules.get(gamemode);
        if(message == null){
            sendBaseComponent(new ComponentBuilder(String.format("Please report in dev-public that %s has no rules set!", gamemode)).color(Style.ERROR).create(),
                    player);
            return 0;
        }
        sendBaseComponent(message, player);
        return 1;
    }

    public static int SetGoal(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        int scoreGoal = c.getArgument(ArgumentNames.SCORE_GOAL, Integer.class);

        GamemodeRunner runner = PVPPlugin.getInstance().getActiveGame();

        ((ScoreGoal) runner).setScoreGoal(scoreGoal);
        sendBaseComponent(
            new ComponentBuilder(String.format("Goal set to %d for %s on %s."
                , scoreGoal, runner.getGamemode(), runner.getMapName()))
                .color(Style.INFO).create(),
            player);
        return 1;
    }

    public static int SetTimeLimit(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        int timeLimit = c.getArgument(ArgumentNames.TIME_LIMIT, Integer.class);

        GamemodeRunner runner = PVPPlugin.getInstance().getActiveGame();

        ((TimeLimit) runner).setTimeLimit(timeLimit);
        sendBaseComponent(
                new ComponentBuilder(String.format("Time limit set to %d for %s on %s."
                        , timeLimit, runner.getGamemode(), runner.getMapName()))
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
