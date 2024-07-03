package com.mcmiddleearth.pvpplugin.command.argumentTypes;

import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.statics.ArgumentNames;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.concurrent.CompletableFuture;

public class InstanceOfClassIntegerArgument<T> implements ArgumentType<Integer> {
    final IntegerArgumentType innerArgument;
    final Class<T> tClass;
    String result;

    public InstanceOfClassIntegerArgument(Class<T> tClass){
        this.tClass = tClass;
        innerArgument = IntegerArgumentType.integer();
    }

    public InstanceOfClassIntegerArgument(int min, Class<T> tClass){
        innerArgument = IntegerArgumentType.integer(min);
        this.tClass = tClass;
    }
    public InstanceOfClassIntegerArgument(int min, int max, Class<T> tClass){
        this.tClass = tClass;
        innerArgument = IntegerArgumentType.integer(min, max);
    }

    @Override
    public Integer parse(StringReader reader) throws CommandSyntaxException {
        if(result != null)
            return reader.readInt();
        LiteralMessage message = new LiteralMessage(String.format("Gamemode doesn't extend %s", tClass.getTypeName()));
        throw new CommandSyntaxException(
                new SimpleCommandExceptionType(message), message);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String gamemode;
        try {
            gamemode = context.getArgument(ArgumentNames.GAMEMODE, String.class);
        } catch (Exception e) {
            result = builder.getRemaining();
            return builder.buildFuture();
        }

        Class<? extends GamemodeRunner> runner = Gamemodes.getRunners.get(gamemode);
        if (runner == null || !tClass.isAssignableFrom(runner))
            return builder.buildFuture();
        result = builder.getRemaining();
        return builder.buildFuture();
    }
}
