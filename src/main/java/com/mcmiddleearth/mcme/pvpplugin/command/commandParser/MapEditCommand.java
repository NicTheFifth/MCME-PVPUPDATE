package com.mcmiddleearth.mcme.pvpplugin.command.commandParser;

import com.google.common.base.Joiner;
import com.mcmiddleearth.command.AbstractCommandHandler;
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

public class MapEditCommand extends AbstractCommandHandler implements TabExecutor {

    PVPPlugin pvpPlugin;

    public MapEditCommand(String command, PVPPlugin pvpPlugin) {
        super(command);
        this.pvpPlugin = pvpPlugin;
    }

    @Override
    protected HelpfulLiteralBuilder createCommandTree(HelpfulLiteralBuilder commandNodeBuilder) {
        commandNodeBuilder
            .requires(Requirements::isMapEditor)
            .then(HelpfulLiteralBuilder.literal("edit")
                .then(Arguments.getMap(pvpPlugin)
                        .executes(c->EditExecutor.NewMapEditor(pvpPlugin,c))))
            .then(HelpfulLiteralBuilder.literal("create")
                .then(HelpfulRequiredArgumentBuilder.argument("map", StringArgumentType.string())
                        .executes(c -> EditExecutor.CreateMapEditor(pvpPlugin, c))))
            .then(HelpfulLiteralBuilder.literal("setarea"))
            .then(HelpfulLiteralBuilder.literal("title"))
            .then(HelpfulLiteralBuilder.literal("setrp")
                .then(Arguments.rpArgument()))
            .then(HelpfulLiteralBuilder.literal("setSpawn")
                .then(Arguments.spawnArgumentRB()
                        .requires(c -> Requirements.hasRB(c,pvpPlugin)))
                .then(Arguments.spawnArgumentIS()
                        .requires(c -> Requirements.hasIS(c,pvpPlugin)))
                .then(Arguments.spawnArgumentDR()
                        .requires(c->Requirements.hasDR(c,pvpPlugin))))
            .then(HelpfulLiteralBuilder.literal("gamemode")
                .then(Arguments.getGamemodes(pvpPlugin)))
            .then(HelpfulLiteralBuilder.literal("setMax")
                .requires(c-> Requirements.stageGamemode(c,pvpPlugin))
                .then(HelpfulRequiredArgumentBuilder.argument("amount",IntegerArgumentType.integer(1))))
            .then(HelpfulLiteralBuilder.literal("setGoal")
                .requires(c-> Requirements.canEditGoal(c,pvpPlugin)))
            .then(HelpfulLiteralBuilder.literal("addCapture")
                .requires(c -> Requirements.canEditCapture(c,pvpPlugin)))
            .then(HelpfulLiteralBuilder.literal("delCapture")
                .requires(c -> Requirements.canEditCapture(c,pvpPlugin))
                .then(HelpfulRequiredArgumentBuilder.argument("pointNum",IntegerArgumentType.integer(0))))
            ;
        return commandNodeBuilder;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        PVPCommandSender wrappedSender = new PVPCommandSender(sender, pvpPlugin);
        execute(wrappedSender, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        TabCompleteRequest request = new SimpleTabCompleteRequest(new PVPCommandSender(sender, pvpPlugin),
                String.format("/%s %s", alias, Joiner.on(' ').join(args)).trim());
        onTabComplete(request);
        return request.getSuggestions();
    }
}
