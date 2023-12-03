package com.mcmiddleearth.pvpplugin.command.commandParser;

import com.google.common.base.Joiner;
import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.pvpplugin.command.PVPCommandSender;
import com.mcmiddleearth.pvpplugin.command.executor.GameExecutor;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
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
                //TODO: Fix perms
                .then(HelpfulLiteralBuilder.literal("join")
                    .executes(GameExecutor::joinGame))
                .then(HelpfulLiteralBuilder.literal("rules")
                    .then(Arguments.GetGamemodes()
                        .executes(GameExecutor::getRule)))
                .then(HelpfulLiteralBuilder.literal("stats")
                    .then(HelpfulLiteralBuilder.literal("delete")
                        .requires(Requirements::canRun)
                        .then(HelpfulLiteralBuilder.literal("USER"))))
                .then(HelpfulLiteralBuilder.literal("map")
                    .then(HelpfulLiteralBuilder.literal("list")
                        .executes(GameExecutor::listMaps)))
                .then(HelpfulLiteralBuilder.literal("kick")
                        .requires(Requirements::canRun))
                .then(HelpfulLiteralBuilder.literal("create")
                    .requires(Requirements::canRun)
                    .executes(GameExecutor::createGame)
                    .then(Arguments.ExistingMap()
                        .executes(GameExecutor::createGame)
                        .then(Arguments.ExistingGamemode()
                            .executes(GameExecutor::createGame)
                            .then(HelpfulRequiredArgumentBuilder.argument("var", IntegerArgumentType.integer(1))
                                .executes(GameExecutor::createGame)))))
                .then(HelpfulLiteralBuilder.literal("load")
                    .requires(Requirements::canRun)
                    .then(HelpfulLiteralBuilder.literal("public")
                        .executes(GameExecutor::loadPublic))
                    .then(HelpfulLiteralBuilder.literal("private")
                        .executes(GameExecutor::loadPrivate))
                    .then(HelpfulLiteralBuilder.literal("map")
                        .then(Arguments.ExistingMap()
                            .executes(GameExecutor::loadMap)))
                    .then(HelpfulLiteralBuilder.literal("gamemode")
                        .then(Arguments.ExistingGamemode()
                            .executes(GameExecutor::loadGamemode))))
                .then(HelpfulLiteralBuilder.literal("start")
                    .requires(Requirements::canRun)
                    .executes(GameExecutor::startGame))
                .then(HelpfulLiteralBuilder.literal("end")
                    .requires(Requirements::canRun)
                    .executes(GameExecutor::endGame));
        return commandNodeBuilder;
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
