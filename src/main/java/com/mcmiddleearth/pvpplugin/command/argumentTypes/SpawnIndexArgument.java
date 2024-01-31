package com.mcmiddleearth.pvpplugin.command.argumentTypes;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.GamemodeEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.SpawnListEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.TeamSpawnListEditor;
import com.mcmiddleearth.pvpplugin.statics.ArgumentNames;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.mcmiddleearth.pvpplugin.command.executor.EditExecutor.getMapEditor;

public class SpawnIndexArgument implements ArgumentType<Integer> {
    Integer maxInt;
    @Override
    public Integer parse(StringReader reader) throws CommandSyntaxException {
        int value = reader.readInt();
        if(value < maxInt)
            return Integer.valueOf(reader.readUnquotedString());
        LiteralMessage message = new LiteralMessage(
            value + " is a not a valid index, please select a valid index.");
        throw new CommandSyntaxException(
            new SimpleCommandExceptionType(message), message);
    }
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Player player = CommandUtil.getPlayer((McmeCommandSender) context.getSource());
        Optional<MapEditor> mapEditor = getMapEditor(player);
        if(!mapEditor.isPresent())
            return builder.buildFuture();
        GamemodeEditor gamemodeEditor = mapEditor.get().getGamemodeEditor();
        if(gamemodeEditor instanceof SpawnListEditor)
            maxInt = ((SpawnListEditor) gamemodeEditor).amountOfSpawns();
        if(gamemodeEditor instanceof TeamSpawnListEditor) {
            String spawnName =
                context.getArgument(ArgumentNames.GAMEMODE_SPAWN, String.class);
            maxInt =
                ((TeamSpawnListEditor) gamemodeEditor).getSpawnListNames().get(spawnName)
                    .getIndex.get();
        }

        for(int i = 0; i < maxInt; i++)
            if (String.valueOf(i).startsWith(builder.getRemaining()))
                builder.suggest(i);

        return builder.buildFuture();
    }
}
