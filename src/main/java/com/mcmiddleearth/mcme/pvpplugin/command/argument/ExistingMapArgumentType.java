
package com.mcmiddleearth.mcme.pvp.command;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
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

public class ExistingMapArgumentType implements ArgumentType<String> {

    static Set<String> Maps;

    public ExistingMapArgumentType() { Maps = PVPPlugin.getMaps().keySet(); }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String o = reader.readString();
        if (Maps.contains(o)) {
            return o;
        }
        throw new CommandSyntaxException(new SimpleCommandExceptionType(new LiteralMessage("Failed parsing during action evaluation")), new LiteralMessage("Failed parsing during action evaluation on action:" + o));
    }

    @Override
    public Collection<String> getExamples() {
        return Maps;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        for (String option : Maps) {
            if (option.startsWith(builder.getRemaining())) {
                builder.suggest(option);
            }
        }
        return builder.buildFuture();
    }
    public static void UpdateOptions(){
        Maps = PVPPlugin.getMaps().keySet();
    }
}