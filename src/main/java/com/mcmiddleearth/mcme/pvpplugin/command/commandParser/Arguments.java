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
    static HelpfulRequiredArgumentBuilder<String> existingGamemode(PVPPlugin pvpPlugin) {
        return HelpfulRequiredArgumentBuilder.argument("gamemode", NonExistingGamemode(pvpPlugin));
    }

    static NonExistingGamemodeArgument NonExistingGamemode(PVPPlugin pvpPlugin){
        return new NonExistingGamemodeArgument(pvpPlugin);
    }

    static HelpfulRequiredArgumentBuilder<String> nonExistingGamemode(PVPPlugin pvpPlugin) {
        return HelpfulRequiredArgumentBuilder.argument("gamemode", NonExistingGamemode(pvpPlugin));
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
