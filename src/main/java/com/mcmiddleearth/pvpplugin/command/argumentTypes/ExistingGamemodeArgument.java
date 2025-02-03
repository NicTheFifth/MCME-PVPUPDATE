package com.mcmiddleearth.pvpplugin.command.argumentTypes;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.statics.ArgumentNames;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
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

public class ExistingGamemodeArgument implements ArgumentType<String> {
    Set<String> options = new java.util.HashSet<>();

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String value = reader.readUnquotedString();
        if(options.contains(value))
            return value;
        LiteralMessage message = new LiteralMessage(
                value + " is a not an existing gamemode, please select a existing " +
                        "gamemode.");
        throw new CommandSyntaxException(
                new SimpleCommandExceptionType(message), message);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String mapString = context.getArgument(ArgumentNames.MAP_NAME, String.class);
        JSONMap map = PVPPlugin.getInstance().getMaps().get(mapString);

        if(map.getJSONCaptureTheFlag() != null)
            options.add(Gamemodes.CAPTURETHEFLAG);
        if(map.getJSONDeathRun() != null)
            options.add(Gamemodes.DEATHRUN);
        if(map.getJSONFreeForAll() != null)
            options.add(Gamemodes.FREEFORALL);
        if(map.getJSONInfected() != null)
            options.add(Gamemodes.INFECTED);
        if(map.getJSONOneInTheQuiver() != null)
            options.add(Gamemodes.ONEINTHEQUIVER);
        if(map.getJSONRingBearer() != null)
            options.add(Gamemodes.RINGBEARER);
        if(map.getJSONTeamConquest() != null)
            options.add(Gamemodes.TEAMCONQUEST);
        if(map.getJSONTeamDeathMatch() != null)
            options.add(Gamemodes.TEAMDEATHMATCH);
        if(map.getJSONTeamSlayer() != null)
            options.add(Gamemodes.TEAMSLAYER);

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
