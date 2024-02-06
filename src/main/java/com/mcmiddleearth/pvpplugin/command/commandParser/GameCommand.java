package com.mcmiddleearth.pvpplugin.command.commandParser;

import com.google.common.base.Joiner;
import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
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

public class GameCommand extends AbstractCommandHandler implements TabExecutor {
    public GameCommand(String command){
        super(command);
    }
    @Override
    protected HelpfulLiteralBuilder createCommandTree(HelpfulLiteralBuilder commandNodeBuilder) {
        commandNodeBuilder
            .requires(Requirements::isMapEditor)
            .requires(sender -> ((PVPCommandSender)sender).getSender() instanceof Player)
            .then(HelpfulLiteralBuilder.literal("create")
                .requires(Requirements::canRun)
                .then(Arguments.ValidMap()
                    .then(Arguments.ValidGamemode()
                        .executes(GameExecutor::CreateGame))))
            .then(ActiveGameExistsLiteral("start")
                .executes(GameExecutor::StartGame))
            .then(ActiveGameExistsLiteral("join")
                .executes(GameExecutor::JoinGame))
            .then(HelpfulLiteralBuilder.literal("setgoal")
                .requires(Requirements::hasScoreGoal)
                .then(HelpfulRequiredArgumentBuilder.argument(ArgumentNames.GOAL,
                    IntegerArgumentType.integer(0))
                    .executes(GameExecutor::SetGoal)));
        return commandNodeBuilder;
    }
    private LiteralArgumentBuilder<McmeCommandSender> ActiveGameExistsLiteral(String literal){
        return HelpfulLiteralBuilder.literal(literal)
            .requires(Requirements::ActiveGameExists);
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
