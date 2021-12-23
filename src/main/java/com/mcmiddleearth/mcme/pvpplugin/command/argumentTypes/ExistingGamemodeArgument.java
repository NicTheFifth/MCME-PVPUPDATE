package com.mcmiddleearth.mcme.pvpplugin.command.argumentTypes;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ExistingGamemodeArgument implements ArgumentType<String> {
    PVPPlugin pvpPlugin;

    public ExistingGamemodeArgument(PVPPlugin pvpPlugin){
        this.pvpPlugin = pvpPlugin;
    }
    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readUnquotedString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String mapString = context.getArgument("map", String.class);
        JSONMap map = pvpPlugin.getMaps().get(mapString);
        Set<String> options = new java.util.HashSet<>(Collections.emptySet());

        if(map.getJSONCaptureTheFlag() != null)
            options.add("CaptureTheFlag");
        if(map.getJSONDeathRun() != null)
            options.add("DeathRun");
        if(map.getJSONFreeForAll() != null)
            options.add("FreeForAll");
        if(map.getJSONInfected() != null)
            options.add("Infected");
        if(map.getJSONTeamConquest() != null)
            options.add("TeamConquest");
        if(map.getJSONTeamDeathMatch() != null)
            options.add("TeamDeathMatch");
        if(map.getJSONTeamSlayer() != null)
            options.add("TeamSlayer");

        for (String option : options) {
            if (option.startsWith(builder.getRemaining())) {
                builder.suggest(option);
            }
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return ArgumentType.super.getExamples();
    }
}
