package com.mcmiddleearth.pvpplugin.command.commandParser;

import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.command.argumentTypes.*;

import java.util.HashSet;

public class Arguments {

    public static HelpfulRequiredArgumentBuilder<String> getGamemodes() {
        return HelpfulRequiredArgumentBuilder.argument("gamemode", gamemodes());
    }
    private static CommandStringArgument gamemodes(){
        return new CommandStringArgument(PVPPlugin.getInstance().getAvailableGamemodes());
    }

    public static HelpfulRequiredArgumentBuilder<String> getMap() {
        return HelpfulRequiredArgumentBuilder.argument("map", maps());
    }
    private static CommandStringArgument maps(){
        return new CommandStringArgument(new HashSet<>(PVPPlugin.getInstance().getMaps().keySet()));
    }

    public static HelpfulRequiredArgumentBuilder<String> rpArgument() {
        return HelpfulRequiredArgumentBuilder.argument("rp", resourcePacks());
    }
    private static CommandStringArgument resourcePacks() {
        return new CommandStringArgument("dwarven", "eriador","rohan","paths","human");
    }

    public static HelpfulRequiredArgumentBuilder<String> spawnArgument() {
        return HelpfulRequiredArgumentBuilder.argument("spawn", spawns());
    }
    private static SpawnArgument spawns() {
        return new SpawnArgument();
    }

    public static HelpfulRequiredArgumentBuilder<String> NewMapArgument() {
        return HelpfulRequiredArgumentBuilder.argument("map", nonExistingMap());
    }
    private static NonExistingMapArgument nonExistingMap() {
        return new NonExistingMapArgument();
    }

    public static HelpfulRequiredArgumentBuilder<String> getExistingGamemode() {
        return HelpfulRequiredArgumentBuilder.argument("gamemode", existingGamemode());
    }
    private static ExistingGamemodeArgument existingGamemode() {
        return new ExistingGamemodeArgument();
    }

    public static HelpfulRequiredArgumentBuilder<String> getExistingGamemodeAlter() {
        return HelpfulRequiredArgumentBuilder.argument("gamemode", existingGamemodeAlter());
    }
    private static ExistingGamemodeAlterArgument existingGamemodeAlter() {
        return new ExistingGamemodeAlterArgument();
    }
}
