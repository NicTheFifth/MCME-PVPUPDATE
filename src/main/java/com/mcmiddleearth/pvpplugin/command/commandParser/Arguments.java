package com.mcmiddleearth.pvpplugin.command.commandParser;

import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.command.Validator;
import com.mcmiddleearth.pvpplugin.statics.ArgumentNames;
import com.mcmiddleearth.pvpplugin.command.argumentTypes.*;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import com.mcmiddleearth.pvpplugin.statics.Resourcepacks;

import java.util.Map;
import java.util.stream.Collectors;

public class Arguments {
    public static HelpfulRequiredArgumentBuilder<String> ExistingMap() {
        return HelpfulRequiredArgumentBuilder.argument(ArgumentNames.MAP_NAME, ExistingMaps());
    }

    private static CommandStringArgument ExistingMaps() {
        return new CommandStringArgument(PVPPlugin.getInstance().getMaps().keySet());
    }
    public static HelpfulRequiredArgumentBuilder<String> ValidMap(){
        return HelpfulRequiredArgumentBuilder.argument(ArgumentNames.MAP_NAME,
            ValidMaps());
    }
    private static CommandStringArgument ValidMaps(){
        return new CommandStringArgument(
            PVPPlugin.getInstance().getMaps().entrySet().stream()
                .filter(stringJSONMapEntry ->
                    Validator.isMapValid(stringJSONMapEntry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet())
        );
    }
    public static HelpfulRequiredArgumentBuilder<String> NonExistingMap(){
        return HelpfulRequiredArgumentBuilder.argument(ArgumentNames.MAP_NAME, new NonExistingMapArgument());
    }
    public static HelpfulRequiredArgumentBuilder<String> RPArgument(){
        return HelpfulRequiredArgumentBuilder.argument(ArgumentNames.RESOURCEPACK,
            ResourcePacks());
    }
    private static CommandStringArgument ResourcePacks(){
        return new CommandStringArgument(Resourcepacks.getAll);
    }
    public static HelpfulRequiredArgumentBuilder<String> GetGamemodes(){
        return HelpfulRequiredArgumentBuilder.argument(ArgumentNames.GAMEMODE,
            Gamemodes());
    }

    private static CommandStringArgument Gamemodes(){
        return new CommandStringArgument(Gamemodes.getAll);
    }
    public static HelpfulRequiredArgumentBuilder<String> ValidGamemode(){
        return HelpfulRequiredArgumentBuilder.argument(ArgumentNames.GAMEMODE,
            ValidGamemodes());
    }
    public static ValidGamemodeArgument ValidGamemodes() {
        return new ValidGamemodeArgument();
    }
    public static HelpfulRequiredArgumentBuilder<Integer> spawnIndexArgument() {
        return HelpfulRequiredArgumentBuilder.argument(ArgumentNames.INDEX,
            new SpawnIndexArgument());
    }

    public static HelpfulRequiredArgumentBuilder<String> spawnNameArgument() {
        return HelpfulRequiredArgumentBuilder.argument(ArgumentNames.GAMEMODE_SPAWN,
            new SpawnArgument());
    }

    public static HelpfulRequiredArgumentBuilder<String> SpecialPointArgument() {
        return HelpfulRequiredArgumentBuilder.argument(ArgumentNames.SPECIAL_POINT,
        new SpecialPointArgument());
    }

    public static HelpfulRequiredArgumentBuilder<Integer> SpecialPointListIndexArgument() {
        return HelpfulRequiredArgumentBuilder.argument(ArgumentNames.INDEX,
            new SpecialPointIndexArgument());
    }

    public static HelpfulRequiredArgumentBuilder<Integer> TimeLimitArgument(){
        return HelpfulRequiredArgumentBuilder.argument(ArgumentNames.TIME_LIMIT,
                new TimeLimitArgument(0));
    }

    public static HelpfulRequiredArgumentBuilder<Integer> ScoreGoalArgument(){
        return HelpfulRequiredArgumentBuilder.argument(ArgumentNames.SCORE_GOAL,
                new ScoreGoalArgument(0));
    }
}
