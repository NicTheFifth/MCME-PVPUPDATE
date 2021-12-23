package com.mcmiddleearth.mcme.pvpplugin.command.commandParser;

import com.google.common.base.Joiner;
import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.command.PVPCommandSender;
import com.mcmiddleearth.mcme.pvpplugin.command.argumentTypes.CommandStringArgument;
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
                .then(Arguments.getMap(pvpPlugin)
                        .then(HelpfulLiteralBuilder.literal("delete"))
                        .then(HelpfulLiteralBuilder.literal("setspawn"))
                        .then(HelpfulLiteralBuilder.literal("setrp"))
                        .then(HelpfulLiteralBuilder.literal("setarea"))
                        .then(HelpfulLiteralBuilder.literal("settitle")
                                .then(HelpfulRequiredArgumentBuilder.argument("title", new CommandStringArgument())))
                        .then(Arguments.nonExistingGamemode(pvpPlugin)
                                .then(HelpfulLiteralBuilder.literal("create")))
                        //TODO: Get rest of tree up and running
                );
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
