package com.mcmiddleearth.mcme.pvpplugin.command.commandParser;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.command.argumentTypes.CommandStringArgument;

import java.util.HashSet;

public class Arguments {

    static CommandStringArgument Gamemodes(PVPPlugin pvpPlugin){
        return new CommandStringArgument(pvpPlugin.getAvailableGamemodes());
    }

    static CommandStringArgument Maps(PVPPlugin pvpPlugin){
        return new CommandStringArgument(new HashSet<>(pvpPlugin.getMaps().keySet()));
    }
}
