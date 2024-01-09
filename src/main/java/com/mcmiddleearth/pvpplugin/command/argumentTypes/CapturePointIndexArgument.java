package com.mcmiddleearth.pvpplugin.command.argumentTypes;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.TeamConquestEditor;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.mcmiddleearth.pvpplugin.command.executor.EditExecutor.getMapEditor;

public class CapturePointIndexArgument implements ArgumentType<Integer> {
    @Override
    public Integer parse(StringReader reader) {
        return Integer.valueOf(reader.readUnquotedString());
    }
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Player player =  CommandUtil.getPlayer((McmeCommandSender) context.getSource());
        Optional<MapEditor> mapEditor = getMapEditor(player);
        if(!mapEditor.isPresent()){
            return builder.buildFuture();
        }
        TeamConquestEditor editor =
            (TeamConquestEditor) mapEditor.get().getGamemodeEditor();
        if(editor != null)
            for(int i = 0; i < editor.amountOfCapturePoints(); i++)
                if (String.valueOf(i).startsWith(builder.getRemaining())) {
                    builder.suggest(i);
                }

        return builder.buildFuture();
    }
}
