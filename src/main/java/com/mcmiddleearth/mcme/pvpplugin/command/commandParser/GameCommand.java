package com.mcmiddleearth.mcme.pvpplugin.command.commandParser;

import com.google.common.base.Joiner;
import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.command.PVPCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GameCommand extends AbstractCommandHandler implements TabExecutor {
    PVPPlugin pvpPlugin;
    public GameCommand(String command, PVPPlugin pvpPlugin){
        super(command);
        this.pvpPlugin = pvpPlugin;
    }
    @Override
    protected HelpfulLiteralBuilder createCommandTree(HelpfulLiteralBuilder commandNodeBuilder) {
        commandNodeBuilder
                .then(HelpfulLiteralBuilder.literal("join"))
                .then(HelpfulLiteralBuilder.literal("rules")
                        .then(Arguments.getGamemodes(pvpPlugin)))
                .then(HelpfulLiteralBuilder.literal("stats")
                        .then(HelpfulLiteralBuilder.literal("delete")
                                .then(HelpfulLiteralBuilder.literal("USER"))))
                .then(HelpfulLiteralBuilder.literal("map"))
                .then(HelpfulLiteralBuilder.literal("kick"))
                .then(HelpfulLiteralBuilder.literal("game"));
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
