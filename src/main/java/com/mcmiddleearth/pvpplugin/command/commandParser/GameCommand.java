package com.mcmiddleearth.pvpplugin.command.commandParser;

import com.google.common.base.Joiner;
import com.mcmiddleearth.command.handler.AbstractCommandHandler;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.pvpplugin.command.PVPCommandSender;
import com.mcmiddleearth.pvpplugin.command.executor.GameExecutor;
import com.mcmiddleearth.pvpplugin.statics.ArgumentNames;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class GameCommand extends AbstractCommandHandler implements TabExecutor {
    public GameCommand(String command){
        super(command);
    }
    @Override
    protected HelpfulLiteralBuilder createCommandTree(HelpfulLiteralBuilder commandNodeBuilder) {
        commandNodeBuilder
            .requires(sender -> ((PVPCommandSender)sender).getSender() instanceof Player)
                .then(HelpfulLiteralBuilder.literal("autojoin")
                        .executes(GameExecutor::ToggleAutojoin))
            .then(HelpfulLiteralBuilder.literal("create")
                .requires(Requirements::canRun)
                .then(Arguments.ValidMap()
                    .then(Arguments.ValidGamemode()
                        .executes(GameExecutor::CreateGame)
                        .then(Arguments.TimeLimitArgument()
                            .executes(GameExecutor::CreateGameWithTimeLimit)
                            .then(Arguments.ScoreGoalArgument()
                                .executes(GameExecutor::CreateGameWithTimeLimitAndScoreGoal)))
                        .then(Arguments.ScoreGoalArgument()
                            .executes(GameExecutor::CreateGameWithScoreGoal)))))
            .then(MultiReqLiteral("start",
                    Requirements::canRun, Requirements::ActiveGameExists)
                .executes(GameExecutor::StartGame))
            .then(HelpfulLiteralBuilder.literal("join")
                .requires(Requirements::ActiveGameExists)
                .executes(GameExecutor::JoinGame))
            .then(HelpfulLiteralBuilder.literal("rules")
                   .then(Arguments.GetGamemodes()
                        .executes(GameExecutor::SendRules)))
            .then(MultiReqLiteral("setgoal",
                Requirements::hasScoreGoal,
                Requirements::canRun)
                .then(Arguments.ScoreGoalArgument()
                    .executes(GameExecutor::SetGoal)))
                .then(MultiReqLiteral("settimelimit",
                    Requirements::hasTimeLimit,
                    Requirements::canRun)
                        .then(Arguments.TimeLimitArgument()
                            .executes(GameExecutor::SetTimeLimit)))
            .then(MultiReqLiteral("stop",
                Requirements::ActiveGameExists,
                Requirements::canRun)
                .executes(GameExecutor::EndGame));
        return commandNodeBuilder;
    }

    @SafeVarargs
    private LiteralArgumentBuilder<McmeCommandSender> MultiReqLiteral(String literal, Predicate<McmeCommandSender>... predicates){
        return HelpfulLiteralBuilder.literal(literal)
            .requires(
                CommandUtil.multiRequirements(predicates)
            );
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        PVPCommandSender wrappedSender = new PVPCommandSender(sender);
        execute(wrappedSender, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        TabCompleteRequest request = new SimpleTabCompleteRequest(new PVPCommandSender(sender),
                String.format("/%s %s", alias, Joiner.on(' ').join(args)));
        onTabComplete(request);
        return request.getSuggestions();
    }
}
