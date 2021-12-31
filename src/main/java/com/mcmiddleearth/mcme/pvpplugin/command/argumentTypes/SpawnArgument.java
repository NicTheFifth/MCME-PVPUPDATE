package com.mcmiddleearth.mcme.pvpplugin.command.argumentTypes;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class SpawnArgument implements ArgumentType<String> {

    PVPPlugin pvpPlugin;

    public SpawnArgument(PVPPlugin pvpPlugin){
        this.pvpPlugin = pvpPlugin;
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readUnquotedString();
    }

    @Override
    public Collection<String> getExamples() {
        return ArgumentType.super.getExamples();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {

        return builder.buildFuture();
    }
}
