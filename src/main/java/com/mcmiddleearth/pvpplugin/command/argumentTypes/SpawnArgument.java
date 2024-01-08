package com.mcmiddleearth.pvpplugin.command.argumentTypes;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.GamemodeEditor;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.entity.Player;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SpawnArgument<T> implements ArgumentType<String> {

    private final Class<T> type;
    Set<String> options;
    public SpawnArgument(Class<T> type, String... options){
        this.type = type;
        this.options = Set.of(options);
    }
    @Override
    public String parse(StringReader reader) {
        return reader.readUnquotedString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        Player source = (Player) context.getSource();
        if(source == null)
            return builder.buildFuture();
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null)
            return builder.buildFuture();
        GamemodeEditor gamemodeEditor = me.getGamemodeEditor();
        if(gamemodeEditor == null ||
            !this.type.isInstance(gamemodeEditor.getClass()))
            return builder.buildFuture();
//
//        if(Objects.equals(map.getGamemode(), "Free For All"))
//            options.add("spawn");
//
//        if(Objects.equals(map.getGamemode(), "Deathrun")){
//            options.add("death");
//            options.add("runner");
//        }
//        if(Objects.equals(map.getGamemode(), "Infected")){
//            options.add("infected");
//            options.add("survivor");
//        }


        for (String option : options) {
            if (option.startsWith(builder.getRemaining())) {
                builder.suggest(option);
            }
        }
        return builder.buildFuture();
    }
}
