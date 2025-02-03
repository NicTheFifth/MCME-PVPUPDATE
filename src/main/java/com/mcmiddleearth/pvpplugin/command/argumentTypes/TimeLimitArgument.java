package com.mcmiddleearth.pvpplugin.command.argumentTypes;

import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.TimeLimit;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.concurrent.CompletableFuture;

public class TimeLimitArgument implements ArgumentType<Integer> {

    InstanceOfClassIntegerArgument<TimeLimit> innerArgument;

    public TimeLimitArgument(){
        innerArgument = new InstanceOfClassIntegerArgument<>(TimeLimit.class);
    }
    public TimeLimitArgument(int min){
        innerArgument = new InstanceOfClassIntegerArgument<>(min,TimeLimit.class);
    }
    public TimeLimitArgument(int min, int max){
        innerArgument = new InstanceOfClassIntegerArgument<>(min,max,TimeLimit.class);
    }

    @Override
    public Integer parse(StringReader reader) throws CommandSyntaxException {
        return innerArgument.parse(reader);
    }
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        builder.suggest("Time Limit Argument (seconds)");
        return innerArgument.listSuggestions(context, builder);
    }
}
