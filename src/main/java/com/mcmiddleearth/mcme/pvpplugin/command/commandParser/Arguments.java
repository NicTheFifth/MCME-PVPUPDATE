package com.mcmiddleearth.mcme.pvpplugin.command.commandParser;

import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.command.argumentTypes.*;

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

    public static HelpfulRequiredArgumentBuilder<String> spawnArgumentRB() {
        return HelpfulRequiredArgumentBuilder.argument("spawn", spawnsRB());
    }
    private static CommandStringArgument spawnsRB() {
        return new CommandStringArgument("red","blue");
    }

    public static HelpfulRequiredArgumentBuilder<String> spawnArgumentDR() {
        return HelpfulRequiredArgumentBuilder.argument("spawn", spawnsDR());
    }
    private static CommandStringArgument spawnsDR() {
        return new CommandStringArgument("death, runner");
    }

    public static HelpfulRequiredArgumentBuilder<String> spawnArgumentIS() {
        return HelpfulRequiredArgumentBuilder.argument("spawn", spawnsIS());
    }
    private static CommandStringArgument spawnsIS() {
        return new CommandStringArgument("infected", "survivor");
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
}
