package com.mcmiddleearth.pvpplugin.command.argumentTypes;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.command.Validator;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ValidMapArgument  implements ArgumentType<String> {

    static volatile Set<String> options;

    public ValidMapArgument() { options = PVPPlugin.getInstance().getMapNames(); }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String o = reader.readString();
        if(!options.contains(o) || !Validator.isMapValid(PVPPlugin.getInstance().getMaps().get(o))){
            LiteralMessage message = new LiteralMessage(
                    o + " doesn't exist, please select an existing map.");
            throw new CommandSyntaxException(
                    new SimpleCommandExceptionType(message), message);
        }
        return o;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        options.forEach(option -> {
            if(option.startsWith(builder.getRemaining()) && Validator.isMapValid(PVPPlugin.getInstance().getMaps().get(option)))
                builder.suggest(option);
        });
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return ArgumentType.super.getExamples();
    }
}
