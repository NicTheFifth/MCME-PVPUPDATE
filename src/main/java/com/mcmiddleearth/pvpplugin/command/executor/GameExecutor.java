package com.mcmiddleearth.pvpplugin.command.executor;

import com.mcmiddleearth.command.sender.McmeCommandSender;
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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.function.Supplier;

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
        String text = "you'll automatically join games now.";
        if(!pvpPlugin.getAutojoiners().remove(player)) {
            pvpPlugin.getAutojoiners().add(player);
            text = "you won't automatically join games anymore.";
        }
        player.sendMessage(Component.text().content("Toggled Auto-Join, " + text).color(NamedTextColor.AQUA).build());
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
            Bukkit.getServer().getOnlinePlayers().forEach(p -> {
                if (!pvpPlugin.getAutojoiners().contains(p))
                    activeGame.Join(p);
            });
            return 1;
        }
        pvpPlugin.getGameQueue().add(runner);
        player.sendMessage(MiniMessage.miniMessage().deserialize(
                "<aqua>Game created, added to queue: <gamemode> on <title></aqua>",
                Placeholder.parsed("gamemode", gamemode),
                Placeholder.parsed("title", map.getTitle())));
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
            player.sendMessage(MiniMessage.miniMessage().deserialize(
                    "<aqua>Game created: <gamemode> on <title> with time limit <time></aqua>",
                    Placeholder.parsed("gamemode", activeGame.getGamemode()),
                    Placeholder.parsed("title", activeGame.getMapName()),
                    Placeholder.parsed("time", timeLimit.toString())));
            Bukkit.getServer().getOnlinePlayers().forEach(p -> {
                if (!pvpPlugin.getAutojoiners().contains(p))
                    activeGame.Join(p);
            });
            return 1;
        }
        pvpPlugin.getGameQueue().add(runner);
        player.sendMessage(MiniMessage.miniMessage().deserialize(
                "<aqua>Game created: <gamemode> on <title> with time limit <time></aqua>",
                Placeholder.parsed("gamemode", gamemode),
                Placeholder.parsed("title", map.getTitle()),
                Placeholder.parsed("time", timeLimit.toString())));
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

            player.sendMessage(MiniMessage.miniMessage().deserialize(
                    "<aqua>Game created: <gamemode> on <title> with score goal <score></aqua>",
                    Placeholder.parsed("gamemode", activeGame.getGamemode()),
                    Placeholder.parsed("title", activeGame.getMapName()),
                    Placeholder.parsed("score", scoreGoal.toString())));

            Bukkit.getServer().getOnlinePlayers().forEach(p -> {
                if (!pvpPlugin.getAutojoiners().contains(p))
                    activeGame.Join(p);
            });
            return 1;
        }
        pvpPlugin.getGameQueue().add(runner);
        player.sendMessage(MiniMessage.miniMessage().deserialize(
                "<aqua>Game created: <gamemode> on <title> with score goal <score></aqua>",
                Placeholder.parsed("gamemode", gamemode),
                Placeholder.parsed("title", map.getTitle()),
                Placeholder.parsed("score", scoreGoal.toString())));
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
            player.sendMessage(MiniMessage.miniMessage().deserialize(
                    "<aqua>Game created: <gamemode> on <title> with time limit <time> and score goal <score></aqua>",
                    Placeholder.parsed("gamemode", activeRunner.getGamemode()),
                    Placeholder.parsed("title", activeRunner.getMapName()),
                    Placeholder.parsed("score", scoreGoal.toString()),
                    Placeholder.parsed("time", timeLimit.toString())));
            Bukkit.getServer().getOnlinePlayers().forEach(p -> {
                if (!pvpPlugin.getAutojoiners().contains(p))
                    activeRunner.Join(p);
            });
            return 1;
        }
        pvpPlugin.getGameQueue().add(runner);
        player.sendMessage(MiniMessage.miniMessage().deserialize(
                "<aqua>Game created: <gamemode> on <title> with time limit <time> and score goal <score></aqua>",
                Placeholder.parsed("gamemode", gamemode),
                Placeholder.parsed("title", map.getTitle()),
                Placeholder.parsed("score", scoreGoal.toString()),
                Placeholder.parsed("time", timeLimit.toString())));
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
        Component message = Gamemodes.getRules.get(gamemode);
        if(message == null){
            player.sendMessage(MiniMessage.miniMessage().deserialize(
                    "<red>Please report in dev-public that <gamemode> has no rules set!</red>",
                    Placeholder.parsed("gamemode", gamemode)));
            return 0;
        }
        player.sendMessage(message);
        return 1;
    }

    public static int SendActiveGamemodeRules(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        GamemodeRunner gamemodeRunner = PVPPlugin.getInstance().getActiveGame();
        if(gamemodeRunner == null){
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>There is no active game running.</red>"));
            return 0;
        }
        String gamemode = gamemodeRunner.getGamemode();
        Component message = Gamemodes.getRules.get(gamemode);
        if(message == null){
            player.sendMessage(MiniMessage.miniMessage().deserialize(
                    "<red>Please report in dev-public that <gamemode> has no rules set!</red>",
                    Placeholder.parsed("gamemode", gamemode)));
            return 0;
        }
        player.sendMessage(message);
        return 1;
    }

    public static int SetGoal(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        Integer scoreGoal = c.getArgument(ArgumentNames.SCORE_GOAL, Integer.class);

        GamemodeRunner runner = PVPPlugin.getInstance().getActiveGame();

        ((ScoreGoal) runner).setScoreGoal(scoreGoal);
        player.sendMessage(MiniMessage.miniMessage().deserialize(
                "<aqua>Goal set to <goal> for <gamemode> on <title>.</aqua>",
                Placeholder.parsed("gamemode", runner.getGamemode()),
                Placeholder.parsed("title", runner.getMapName()),
                Placeholder.parsed("goal", scoreGoal.toString())));
        return 1;
    }

    public static int SetTimeLimit(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        Integer timeLimit = c.getArgument(ArgumentNames.TIME_LIMIT, Integer.class);

        GamemodeRunner runner = PVPPlugin.getInstance().getActiveGame();

        ((TimeLimit) runner).setTimeLimit(timeLimit);

        player.sendMessage(MiniMessage.miniMessage().deserialize(
                "<aqua>Time limit set to <time> for <gamemode> on <title>.</aqua>",
                Placeholder.parsed("gamemode", runner.getGamemode()),
                Placeholder.parsed("title", runner.getMapName()),
                Placeholder.parsed("time", timeLimit.toString())));
        return 1;
    }

    public static int EndGame(CommandContext<McmeCommandSender> c) {
        PVPPlugin pvpPlugin = PVPPlugin.getInstance();
        GamemodeRunner runner = pvpPlugin.getActiveGame();

        runner.end(true);

        return 1;
    }
}
