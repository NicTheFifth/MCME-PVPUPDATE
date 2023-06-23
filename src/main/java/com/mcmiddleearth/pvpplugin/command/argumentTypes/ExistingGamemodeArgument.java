package com.mcmiddleearth.pvpplugin.command.argumentTypes;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ExistingGamemodeArgument implements ArgumentType<String> {

    @Override
    public String parse(StringReader reader) {
        return reader.readUnquotedString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String mapString = context.getArgument("map", String.class);
        JSONMap map = PVPPlugin.getInstance().getMaps().get(mapString);
        Set<String> options = new java.util.HashSet<>(Collections.emptySet());

        if(map.getJSONCaptureTheFlag() != null)
            options.add("capturetheflag");
        if(map.getJSONDeathRun() != null)
            options.add("deathrun");
        if(map.getJSONFreeForAll() != null)
            options.add("freeforall");
        if(map.getJSONInfected() != null)
            options.add("infected");
        if(map.getJSONTeamConquest() != null)
            options.add("teamconquest");
        if(map.getJSONTeamDeathMatch() != null)
            options.add("teamdeathmatch");
        if(map.getJSONTeamSlayer() != null)
            options.add("teamslayer");

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
