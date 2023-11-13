package com.mcmiddleearth.pvpplugin.command.commandParser;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.command.argumentTypes.*;
import com.mojang.brigadier.builder.ArgumentBuilder;

import java.util.HashSet;

public class Arguments {
    public static HelpfulRequiredArgumentBuilder<String> ExistingMap() {
        return HelpfulRequiredArgumentBuilder.argument("mapName", ExistingMaps());
    }

    private static CommandStringArgument ExistingMaps() {
        return new CommandStringArgument((HashSet<String>) PVPPlugin.getInstance().getMaps().keySet());
    }

    public static HelpfulRequiredArgumentBuilder<String> NonExistingMap(){
        return HelpfulRequiredArgumentBuilder.argument("mapName", new NonExistingMapArgument());
    }

    public static HelpfulRequiredArgumentBuilder<String> RPArgument(){
        return HelpfulRequiredArgumentBuilder.argument("rp", ResourcePacks());
    }

    private static CommandStringArgument ResourcePacks(){
        return new CommandStringArgument(PVPPlugin.getInstance().getRps());
    }

    public static HelpfulRequiredArgumentBuilder<String> SpawnArgument(){
        return HelpfulRequiredArgumentBuilder.argument("spawn", new SpawnArgument());
    }

    public static HelpfulRequiredArgumentBuilder<String> GetGamemodes(){
        return HelpfulRequiredArgumentBuilder.argument("gamemode", Gamemodes());
    }

    private static CommandStringArgument Gamemodes(){
        return new CommandStringArgument(PVPPlugin.getInstance().getAvailableGamemodes());
    }
}
