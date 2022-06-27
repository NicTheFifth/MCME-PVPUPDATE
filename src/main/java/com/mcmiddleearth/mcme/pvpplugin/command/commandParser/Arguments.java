package com.mcmiddleearth.mcme.pvpplugin.command.commandParser;

import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.command.argumentTypes.*;

import java.util.HashSet;

public class Arguments {

    static CommandStringArgument Gamemodes(){
        return new CommandStringArgument(PVPPlugin.getInstance().getAvailableGamemodes());
    }
    static HelpfulRequiredArgumentBuilder<String> getGamemodes() {
        return HelpfulRequiredArgumentBuilder.argument("gamemode", Gamemodes());
    }

    static CommandStringArgument Maps(){
        return new CommandStringArgument(new HashSet<>(PVPPlugin.getInstance().getMaps().keySet()));
    }
    static HelpfulRequiredArgumentBuilder<String> getMap() {
        return HelpfulRequiredArgumentBuilder.argument("map", Maps());
    }

    public static HelpfulRequiredArgumentBuilder<String> rpArgument() {
        return HelpfulRequiredArgumentBuilder.argument("rp", ResourcePacks());
    }

    private static CommandStringArgument ResourcePacks() {
        return new CommandStringArgument("dwarven", "eriador","rohan","paths","human");
    }

    public static HelpfulRequiredArgumentBuilder<String> spawnArgumentRB() {
        return HelpfulRequiredArgumentBuilder.argument("spawn", SpawnsRB());
    }

    private static CommandStringArgument SpawnsRB() {
        return new CommandStringArgument("red","blue");
    }

    public static HelpfulRequiredArgumentBuilder<String> spawnArgumentDR() {
        return HelpfulRequiredArgumentBuilder.argument("spawn", SpawnsDR());
    }

    private static CommandStringArgument SpawnsDR() {
        return new CommandStringArgument("death, runner");
    }

    public static HelpfulRequiredArgumentBuilder<String> spawnArgumentIS() {
        return HelpfulRequiredArgumentBuilder.argument("spawn", SpawnsIS());
    }

    private static CommandStringArgument SpawnsIS() {
        return new CommandStringArgument("infected", "survivor");
    }

    public static HelpfulRequiredArgumentBuilder<String> NewMapArgument() {
        return HelpfulRequiredArgumentBuilder.argument("map", NonExistingMap());
    }

    private static NonExistingMapArgument NonExistingMap() {
        return new NonExistingMapArgument();
    }
}
