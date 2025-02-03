package com.mcmiddleearth.pvpplugin.command.argumentTypes;

import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.ScoreGoal;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.concurrent.CompletableFuture;

public class ScoreGoalArgument implements ArgumentType<Integer> {

    InstanceOfClassIntegerArgument<ScoreGoal> innerArgument;

    public ScoreGoalArgument() {
        innerArgument = new InstanceOfClassIntegerArgument<>(ScoreGoal.class);
    }

    public ScoreGoalArgument(int min) {
        innerArgument = new InstanceOfClassIntegerArgument<>(min, ScoreGoal.class);
    }

    public ScoreGoalArgument(int min, int max) {
        innerArgument = new InstanceOfClassIntegerArgument<>(min, max, ScoreGoal.class);
    }

    @Override
    public Integer parse(StringReader reader) throws CommandSyntaxException {
        return innerArgument.parse(reader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        builder.suggest("Score goal argument (int)");
        return innerArgument.listSuggestions(context, builder);
    }
}
