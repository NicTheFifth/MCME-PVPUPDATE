package com.mcmiddleearth.mcme.pvpplugin.command.commandParser;

import com.google.common.base.Joiner;
import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.command.PVPCommandSender;
import com.mcmiddleearth.mcme.pvpplugin.command.executor.EditExecutor;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;

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
                .then(HelpfulRequiredArgumentBuilder.argument("map", StringArgumentType.string())
                        .executes(EditExecutor::CreateMapEditor)))
            .then(HelpfulLiteralBuilder.literal("setarea")
                    .executes(EditExecutor::SetArea))
            .then(HelpfulLiteralBuilder.literal("title")
                .then(HelpfulRequiredArgumentBuilder.argument("map", StringArgumentType.string())
                    .executes(EditExecutor::SetTitle)))
            .then(HelpfulLiteralBuilder.literal("setrp")
                .then(Arguments.rpArgument()
                    .executes(EditExecutor::SetRP)))
            .then(HelpfulLiteralBuilder.literal("setSpawn")
                .then(Arguments.spawnArgumentRB()
                        .requires(Requirements::hasRB)
                        .executes(EditExecutor::SetSpawn))
                .then(Arguments.spawnArgumentIS()
                        .requires(Requirements::hasIS)
                        .executes(EditExecutor::SetSpawn))
                .then(Arguments.spawnArgumentDR()
                        .requires(Requirements::hasDR)
                        .executes(EditExecutor::SetSpawn)))
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
        PVPPlugin.getInstance().getLogger().log(Level.INFO, "Wrapped sender is " +PVPCommandSender.wrap(sender).getClass().toString());
        TabCompleteRequest request = new SimpleTabCompleteRequest(PVPCommandSender.wrap(sender),
                String.format("/%s %s", alias, Joiner.on(' ').join(args)).trim());
        onTabComplete(request);
        return request.getSuggestions();
    }
}
