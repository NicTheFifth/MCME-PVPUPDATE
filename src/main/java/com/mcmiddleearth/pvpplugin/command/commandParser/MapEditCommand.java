package com.mcmiddleearth.pvpplugin.command.commandParser;

import com.google.common.base.Joiner;
import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.pvpplugin.command.PVPCommandSender;
import com.mcmiddleearth.pvpplugin.command.executor.EditExecutor;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MapEditCommand extends AbstractCommandHandler implements TabExecutor {

    public MapEditCommand(String command) {
        super(command);
    }

    @Override
    protected HelpfulLiteralBuilder createCommandTree(HelpfulLiteralBuilder commandNodeBuilder) {
        commandNodeBuilder
            .requires(Requirements::isMapEditor)
            .then(HelpfulLiteralBuilder.literal("edit")
                .then(Arguments.getMap()
                        .executes(EditExecutor::NewMapEditor)))
            .then(HelpfulLiteralBuilder.literal("create")
                .then(Arguments.NewMapArgument()
                        .executes(EditExecutor::CreateMap)))
            .then(HelpfulLiteralBuilder.literal("setarea")
                    .executes(EditExecutor::SetArea))
            .then(HelpfulLiteralBuilder.literal("title")
                .then(Arguments.NewMapArgument()
                    .executes(EditExecutor::SetTitle)))
            .then(HelpfulLiteralBuilder.literal("setrp")
                .then(Arguments.rpArgument()
                    .executes(EditExecutor::SetRP)))
            .then(HelpfulLiteralBuilder.literal("setSpawn")
                .executes(EditExecutor::setMapSpawn)
                .then(Arguments.spawnArgument()
                        .executes(EditExecutor::setSpawn)))
            .then(HelpfulLiteralBuilder.literal("gamemode")
                .then(Arguments.getGamemodes()
                        .executes(EditExecutor::SetGamemode)))
            .then(HelpfulLiteralBuilder.literal("setMax")
                .requires(Requirements::stageGamemode)
                .then(HelpfulRequiredArgumentBuilder.argument("amount",IntegerArgumentType.integer(1))
                        .executes(EditExecutor::SetMax)))
            .then(HelpfulLiteralBuilder.literal("setGoal")
                .requires(Requirements::canEditGoal)
                    .executes(EditExecutor::EditGoal))
            .then(HelpfulLiteralBuilder.literal("addCapture")
                .requires(Requirements::canEditCapture)
                    .executes(EditExecutor::CreateCapture))
            .then(HelpfulLiteralBuilder.literal("delCapture")
                .requires(Requirements::canEditCapture)
                .then(HelpfulRequiredArgumentBuilder.argument("pointNum",IntegerArgumentType.integer(0))
                        .executes(EditExecutor::DelCapture)))
            ;
        return commandNodeBuilder;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        McmeCommandSender wrappedSender = new PVPCommandSender(sender);
        execute(wrappedSender, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        TabCompleteRequest request = new SimpleTabCompleteRequest(PVPCommandSender.wrap(sender),
                String.format("/%s %s", alias, Joiner.on(' ').join(args)));
        onTabComplete(request);
        return request.getSuggestions();
    }
}
