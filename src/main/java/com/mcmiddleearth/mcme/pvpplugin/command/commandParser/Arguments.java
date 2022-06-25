package com.mcmiddleearth.mcme.pvpplugin.command.commandParser;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.command.argumentTypes.CommandStringArgument;
import com.mcmiddleearth.mcme.pvpplugin.command.argumentTypes.ExistingGamemodeArgument;
import com.mcmiddleearth.mcme.pvpplugin.command.argumentTypes.NonExistingGamemodeArgument;
import com.mcmiddleearth.mcme.pvpplugin.command.argumentTypes.SpawnArgument;
import com.mojang.brigadier.builder.ArgumentBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

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

    static ExistingGamemodeArgument ExistingGamemode(){
        return new ExistingGamemodeArgument();
    }
    static HelpfulRequiredArgumentBuilder<String> existingGamemode() {
        return HelpfulRequiredArgumentBuilder.argument("gamemode", NonExistingGamemode());
    }

    static NonExistingGamemodeArgument NonExistingGamemode(){
        return new NonExistingGamemodeArgument();
    }

    static HelpfulRequiredArgumentBuilder<String> nonExistingGamemode() {
        return HelpfulRequiredArgumentBuilder.argument("gamemode", NonExistingGamemode());
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
}
