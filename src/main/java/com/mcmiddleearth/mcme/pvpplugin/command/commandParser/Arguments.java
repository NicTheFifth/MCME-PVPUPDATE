package com.mcmiddleearth.mcme.pvpplugin.command.commandParser;

import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.command.argumentTypes.CommandStringArgument;
import com.mcmiddleearth.mcme.pvpplugin.command.argumentTypes.ExistingGamemodeArgument;
import com.mcmiddleearth.mcme.pvpplugin.command.argumentTypes.NonExistingGamemodeArgument;

import java.util.HashSet;
import java.util.Set;

public class Arguments {

    static CommandStringArgument Gamemodes(PVPPlugin pvpPlugin){
        return new CommandStringArgument(pvpPlugin.getAvailableGamemodes());
    }
    static HelpfulRequiredArgumentBuilder<String> getGamemodes(PVPPlugin pvpPlugin) {
        return HelpfulRequiredArgumentBuilder.argument("gamemode", Gamemodes(pvpPlugin));
    }

    static CommandStringArgument Maps(PVPPlugin pvpPlugin){
        return new CommandStringArgument(new HashSet<>(pvpPlugin.getMaps().keySet()));
    }
    static HelpfulRequiredArgumentBuilder<String> getMap(PVPPlugin pvpPlugin) {
        return HelpfulRequiredArgumentBuilder.argument("map", Maps(pvpPlugin));
    }

    static ExistingGamemodeArgument ExistingGamemode(PVPPlugin pvpPlugin){
        return new ExistingGamemodeArgument(pvpPlugin);
    }

    static NonExistingGamemodeArgument NonExistingGamemode(PVPPlugin pvpPlugin){
        return new NonExistingGamemodeArgument(pvpPlugin);
    }

    static HelpfulRequiredArgumentBuilder<String> nonExistingGamemode(PVPPlugin pvpPlugin) {
        return HelpfulRequiredArgumentBuilder.argument("new", NonExistingGamemode(pvpPlugin));
    }
}
