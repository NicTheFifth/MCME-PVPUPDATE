package com.mcmiddleearth.pvpplugin.command.commandParser;

import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.RedBlueTeamEditor;
import com.mcmiddleearth.pvpplugin.statics.ArgumentNames;
import com.mcmiddleearth.pvpplugin.command.argumentTypes.*;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import com.mcmiddleearth.pvpplugin.statics.Resourcepacks;

import java.util.HashSet;

public class Arguments {
    public static HelpfulRequiredArgumentBuilder<String> ExistingMap() {
        return HelpfulRequiredArgumentBuilder.argument(ArgumentNames.MAP_NAME, ExistingMaps());
    }
    private static CommandStringArgument ExistingMaps() {
        return new CommandStringArgument((HashSet<String>) PVPPlugin.getInstance().getMaps().keySet());
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
    public static HelpfulRequiredArgumentBuilder<String> ExistingGamemode(){
        return HelpfulRequiredArgumentBuilder.argument(ArgumentNames.GAMEMODE,
            ExistingGamemodes());
    }
    public static ExistingGamemodeArgument ExistingGamemodes() {
        return new ExistingGamemodeArgument();
    }
    public static HelpfulRequiredArgumentBuilder<Integer> CapturePointIndex(){
        return HelpfulRequiredArgumentBuilder.argument(ArgumentNames.INDEX,
            CapturePointIndexes());
    }

    private static CapturePointIndexArgument CapturePointIndexes() {
        return new CapturePointIndexArgument();
    }

    public static HelpfulRequiredArgumentBuilder<Integer> GetSpawnsFFA() {
        return HelpfulRequiredArgumentBuilder.argument(ArgumentNames.INDEX,
            FFASpawnIndexes());
    }

    private static FFASpawnIndexArgument FFASpawnIndexes(){
        return new FFASpawnIndexArgument();
    }

    public static HelpfulRequiredArgumentBuilder<String> RedBlueSpawnArgument() {
        return HelpfulRequiredArgumentBuilder.argument(ArgumentNames.GAMEMODE_SPAWN,
            new SpawnArgument<>(RedBlueTeamEditor.class,"blue", "red"));
    }
}
