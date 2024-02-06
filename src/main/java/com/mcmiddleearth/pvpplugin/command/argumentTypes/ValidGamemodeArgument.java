package com.mcmiddleearth.pvpplugin.command.argumentTypes;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.command.Validator;
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

public class ValidGamemodeArgument implements ArgumentType<String> {
    Set<String> options = new java.util.HashSet<>();

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException{
        String value = reader.readUnquotedString();
        if(options.contains(value))
            return value;
        LiteralMessage message = new LiteralMessage(
            value + " is a not a valid gamemode, please select a valid " +
                "gamemode.");
        throw new CommandSyntaxException(
            new SimpleCommandExceptionType(message), message);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String mapString = context.getArgument(ArgumentNames.MAP_NAME, String.class);
        JSONMap map = PVPPlugin.getInstance().getMaps().get(mapString);

        if(Validator.canRunCaptureTheFlag(map))
            options.add(Gamemodes.CAPTURETHEFLAG);
        if(Validator.canRunDeathRun(map))
            options.add(Gamemodes.DEATHRUN);
        if(Validator.canRunFreeForAll(map))
            options.add(Gamemodes.FREEFORALL);
        if(Validator.canRunInfected(map))
            options.add(Gamemodes.INFECTED);
        if(Validator.canRunOneInTheQuiver(map))
            options.add(Gamemodes.ONEINTHEQUIVER);
        if(Validator.canRunRingBearer(map))
            options.add(Gamemodes.RINGBEARER);
        if(Validator.canRunTeamConquest(map))
            options.add(Gamemodes.TEAMCONQUEST);
        if(Validator.canRunTeamDeathMatch(map))
            options.add(Gamemodes.TEAMDEATHMATCH);
        if(Validator.canRunTeamSlayer(map))
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
