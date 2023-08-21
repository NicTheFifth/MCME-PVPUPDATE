package com.mcmiddleearth.pvpplugin.command.argumentTypes;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor.EditorState;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SpawnArgument implements ArgumentType<String> {

    public SpawnArgument(){

    }

    @Override
    public String parse(StringReader reader) {
        return reader.readUnquotedString();
    }

    @Override
    public Collection<String> getExamples() {
        return ArgumentType.super.getExamples();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        Player source = CommandUtil.getPlayer((McmeCommandSender) context.getSource());
        if(source == null)
            return builder.buildFuture();
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null)
            return builder.buildFuture();
        MapEditor.EditorState map = me.getState();
        if(map == null)
            return builder.buildFuture();
        Set<String> options = new java.util.HashSet<>(Collections.emptySet());

        if(map == EditorState.CAPTURETHEFLAG ||
                map == EditorState.TEAMCONQUEST ||
                map == EditorState.TEAMDEATHMATCH ||
                map == EditorState.TEAMSLAYER) {
            options.add("blue");
            options.add("red");
        }

        if(map == EditorState.FREEFORALL)
            options.add("spawn");

        if(map == EditorState.DEATHRUN){
            options.add("death");
            options.add("runner");
        }
        if(map == EditorState.INFECTED){
            options.add("infected");
            options.add("survivor");
        }


        for (String option : options) {
            if (option.startsWith(builder.getRemaining())) {
                builder.suggest(option);
            }
        }
        return builder.buildFuture();
    }
}
