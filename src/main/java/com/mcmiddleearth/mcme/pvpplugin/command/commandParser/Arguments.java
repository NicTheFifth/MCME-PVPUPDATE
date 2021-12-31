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

    private static @NotNull CommandStringArgument ResourcePacks() {
        return new CommandStringArgument("dwarven", "eriador","rohan","paths","human");
    }

    private static SpawnArgument SpawnArgument(PVPPlugin pvpPlugin){
        return new SpawnArgument(pvpPlugin);
    }
    public static HelpfulRequiredArgumentBuilder<String> spawnArgument(PVPPlugin pvpPlugin) {

        return HelpfulRequiredArgumentBuilder.argument("spawn", SpawnArgument(pvpPlugin));
    }
}
