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
import java.util.function.Supplier;

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
        Supplier<GamemodeRunner> runner = switch (gamemode) {
            case (Gamemodes.CAPTURETHEFLAG) ->
                    () -> new CaptureTheFlagRunner(map, CaptureTheFlagRunner.GetDefaultScoreGoal(), CaptureTheFlagRunner.GetDefaultTimeLimit());
            case (Gamemodes.DEATHRUN) -> () -> new DeathRunRunner(map, DeathRunRunner.DefaultTimeLimit());
            case (Gamemodes.FREEFORALL) -> () -> new FreeForAllRunner(map, FreeForAllRunner.DefaultTimeLimit());
            case (Gamemodes.INFECTED) -> () -> new InfectedRunner(map, InfectedRunner.DefaultTimeLimit());
            case (Gamemodes.ONEINTHEQUIVER) ->
                    () -> new OneInTheQuiverRunner(map, OneInTheQuiverRunner.DefaultScoreGoal());
            case (Gamemodes.RINGBEARER) -> () -> new RingBearerRunner(map);
            case (Gamemodes.TEAMCONQUEST) -> () -> new TeamConquestRunner(map, TeamConquestRunner.DefaultScoreGoal());
            case (Gamemodes.TEAMDEATHMATCH) -> () -> new TeamDeathmatchRunner(map);
            case (Gamemodes.TEAMSLAYER) -> () -> new TeamSlayerRunner(map, TeamSlayerRunner.DefaultScoreGoal());
            default -> null;
        };
        if(runner == null)
            return 0;
        if(pvpPlugin.getActiveGame() == null) {
            GamemodeRunner activeGame = runner.get();
            pvpPlugin.setActiveGame(activeGame);
            pvpPlugin.getAutojoiners().forEach(activeGame::Join);
            return 1;
        }
        pvpPlugin.getGameQueue().add(runner);
        sendBaseComponent(
            new ComponentBuilder(
                String.format("Game created, added to queue: %s on %s",
                    gamemode, map.getTitle()))
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
        Supplier<GamemodeRunner> runner = switch (gamemode) {
            case (Gamemodes.FREEFORALL) -> () -> new FreeForAllRunner(map, timeLimit);
            case (Gamemodes.CAPTURETHEFLAG) ->
                    () -> new CaptureTheFlagRunner(map, CaptureTheFlagRunner.GetDefaultScoreGoal(), timeLimit);
            case (Gamemodes.DEATHRUN) -> () -> new DeathRunRunner(map, timeLimit);
            case (Gamemodes.INFECTED) -> () -> new InfectedRunner(map, timeLimit);
            default -> null;
        };
        if(runner == null)
            return 0;
        if(pvpPlugin.getActiveGame() == null) {
            GamemodeRunner activeGame = runner.get();
            pvpPlugin.setActiveGame(activeGame);
            sendBaseComponent(
                    new ComponentBuilder(
                            String.format("Game created: %s on %s with time limit %d",
                                    activeGame.getGamemode(), activeGame.getMapName(), timeLimit))
                            .color(Style.INFO)
                            .create(),
                    player);
            pvpPlugin.getAutojoiners().forEach(activeGame::Join);
            return 1;
        }
        pvpPlugin.getGameQueue().add(runner);
        sendBaseComponent(
                new ComponentBuilder(
                        String.format("Game created, added to queue: %s on %s with time limit %d",
                                gamemode, map.getTitle(), timeLimit))
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
        Supplier<GamemodeRunner> runner = switch (gamemode) {
            case (Gamemodes.CAPTURETHEFLAG) ->
                    () -> new CaptureTheFlagRunner(map, scoreGoal, CaptureTheFlagRunner.GetDefaultTimeLimit());
            case (Gamemodes.ONEINTHEQUIVER) -> () -> new OneInTheQuiverRunner(map, scoreGoal);
            case (Gamemodes.TEAMCONQUEST) -> () -> new TeamConquestRunner(map, scoreGoal);
            case (Gamemodes.TEAMSLAYER) -> () -> new TeamSlayerRunner(map, scoreGoal);
            default -> null;
        };
        if(runner == null)
            return 0;
        if(pvpPlugin.getActiveGame() == null) {
            GamemodeRunner activeGame = runner.get();
            pvpPlugin.setActiveGame(activeGame);
            sendBaseComponent(
                    new ComponentBuilder(
                            String.format("Game created: %s on %s with score goal %d",
                                    activeGame.getGamemode(), activeGame.getMapName(), scoreGoal))
                            .color(Style.INFO)
                            .create(),
                    player);
            pvpPlugin.getAutojoiners().forEach(activeGame::Join);
            return 1;
        }
        pvpPlugin.getGameQueue().add(runner);
        sendBaseComponent(
                new ComponentBuilder(
                        String.format("Game created, added to queue: %s on %s with score goal %d",
                                gamemode, map.getTitle(), scoreGoal))
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
        Supplier<GamemodeRunner> runner;
        if(Objects.equals(gamemode, Gamemodes.CAPTURETHEFLAG))
            runner = () -> new CaptureTheFlagRunner(map, scoreGoal, timeLimit);
        else
            return 0;
        if(pvpPlugin.getActiveGame() == null) {
            GamemodeRunner activeRunner = runner.get();
            pvpPlugin.setActiveGame(activeRunner);
            sendBaseComponent(
                    new ComponentBuilder(
                            String.format("Game created: %s on %s with time limit %d and score goal %d",
                                    activeRunner.getGamemode(), activeRunner.getMapName(), timeLimit, scoreGoal))
                            .color(Style.INFO)
                            .create(),
                    player);
            pvpPlugin.getAutojoiners().forEach(activeRunner::Join);
            return 1;
        }
        pvpPlugin.getGameQueue().add(runner);
        sendBaseComponent(
                new ComponentBuilder(
                        String.format("Game created: %s on %s with time limit %d and score goal %d",
                                gamemode, map.getTitle(), timeLimit, scoreGoal))
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

        return 1;
    }
}
