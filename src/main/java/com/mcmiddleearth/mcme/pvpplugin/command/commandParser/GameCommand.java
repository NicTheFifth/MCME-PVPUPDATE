package com.mcmiddleearth.mcme.pvpplugin.command.commandParser;

import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;

public class GameCommand extends AbstractCommandHandler {
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
                        .then(HelpfulRequiredArgumentBuilder.argument("gamemode",Arguments.Gamemodes(pvpPlugin))))
                .then(HelpfulLiteralBuilder.literal("stats"))
                .then(HelpfulLiteralBuilder.literal("map"))
                .then(HelpfulLiteralBuilder.literal("kick"))
                .then(HelpfulLiteralBuilder.literal("game"));
        return commandNodeBuilder;
    }
}
