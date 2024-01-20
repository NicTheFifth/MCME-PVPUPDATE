package com.mcmiddleearth.pvpplugin.command.commandParser.gamemodeEditCommands;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;

public interface GamemodeEditCommand {
    static HelpfulLiteralBuilder addToCommandTree(HelpfulLiteralBuilder commandNodeBuilder){
        return commandNodeBuilder;
    };
}
