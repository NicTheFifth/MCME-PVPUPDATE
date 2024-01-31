package com.mcmiddleearth.pvpplugin.command.argumentTypes;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.RedBlueSpawnListEditor;
import com.mcmiddleearth.pvpplugin.statics.ArgumentNames;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.mcmiddleearth.pvpplugin.command.executor.EditExecutor.getMapEditor;

public class RedBlueSpawnIndexArgument implements ArgumentType<Integer> {
    @Override
    public Integer parse(StringReader reader) {
        return Integer.valueOf(reader.readUnquotedString());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Player player = CommandUtil.getPlayer((McmeCommandSender) context.getSource());
        String spawn = context.getArgument(ArgumentNames.GAMEMODE_SPAWN,
            String.class);
        Optional<MapEditor> mapEditor = getMapEditor(player);
        if (!mapEditor.isPresent())
            return builder.buildFuture();
        if (!(mapEditor.get().getGamemodeEditor() instanceof RedBlueSpawnListEditor))
            return builder.buildFuture();

        RedBlueSpawnListEditor editor =
            (RedBlueSpawnListEditor) mapEditor.get().getGamemodeEditor();
//        if (Objects.equals(spawn, RedBlueSpawnListEditor.BlueSpawn()))
//            for (int i = 0; i < editor.amountOfBlueSpawns(); i++)
//                if (String.valueOf(i).startsWith(builder.getRemaining()))
//                    builder.suggest(i);
//        if (Objects.equals(spawn, RedBlueSpawnListEditor.RedSpawn()))
//            for (int i = 0; i < editor.amountOfRedSpawns(); i++)
//                if (String.valueOf(i).startsWith(builder.getRemaining()))
//                    builder.suggest(i);

        return builder.buildFuture();
    }
}
