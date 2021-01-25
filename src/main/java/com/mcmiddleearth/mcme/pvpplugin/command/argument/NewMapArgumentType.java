package com.mcmiddleearth.mcme.pvpplugin.command.argument;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class NewMapArgumentType implements ArgumentType<String>,  HelpfulArgumentType{
    //TODO: make this argument type working
    private String tooltip = null;

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String o =  reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        return o;
    }

    @Override
    public Collection<String> getExamples() {
        return Collections.singletonList("used inappropriate language");
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        if(tooltip != null) {
            builder.suggest("Explain the inappropriate behaviour of " + context.getArgument("player", String.class),
                    new LiteralMessage(tooltip));
        } else {
            builder.suggest("Explain the inappropriate behaviour of " + context.getArgument("player", String.class));
        }
        return builder.buildFuture();
    }

    @Override
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public String getTooltip() {
        return tooltip;
    }
}
