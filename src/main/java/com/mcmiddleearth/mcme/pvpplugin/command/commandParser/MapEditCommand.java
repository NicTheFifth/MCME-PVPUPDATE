package com.mcmiddleearth.mcme.pvpplugin.command.commandParser;

import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;

public class MapEditCommand extends AbstractCommandHandler {

    PVPPlugin pvpPlugin;

    public MapEditCommand(String command, PVPPlugin pvpPlugin) {
        super(command);
        this.pvpPlugin = pvpPlugin;
    }

    @Override
    protected HelpfulLiteralBuilder createCommandTree(HelpfulLiteralBuilder commandNodeBuilder) {
        commandNodeBuilder
                .then(HelpfulRequiredArgumentBuilder.argument("map", Arguments.Maps(pvpPlugin))
                        .then(HelpfulLiteralBuilder.literal("delete"))
                        .then(HelpfulLiteralBuilder.literal("gamemode")
                                .then(HelpfulRequiredArgumentBuilder.argument("gamemode", Arguments.Gamemodes(pvpPlugin))))
                        .then(HelpfulLiteralBuilder.literal("max")
                                .then(HelpfulLiteralBuilder.literal("Integer")))
                        .then(HelpfulLiteralBuilder.literal("name")
                                .then(HelpfulLiteralBuilder.literal("Name")))
                        .then(HelpfulLiteralBuilder.literal("rp")
                                .then(HelpfulLiteralBuilder.literal("RP")))
                        .then(HelpfulLiteralBuilder.literal("setarea"))
                        .then(HelpfulLiteralBuilder.literal("spawn")
                                .then(HelpfulLiteralBuilder.literal("list"))
                                .then(HelpfulLiteralBuilder.literal("show"))
                                .then(HelpfulLiteralBuilder.literal("hide"))
                                .then(HelpfulLiteralBuilder.literal("SpawnName")
                                        .then(HelpfulLiteralBuilder.literal("delete"))
                                        .then(HelpfulLiteralBuilder.literal("create"))
                                        .then(HelpfulLiteralBuilder.literal("setloc"))))
                        .then(HelpfulLiteralBuilder.literal("title")
                                .then(HelpfulLiteralBuilder.literal("Title"))));
        return commandNodeBuilder;
    }
}
