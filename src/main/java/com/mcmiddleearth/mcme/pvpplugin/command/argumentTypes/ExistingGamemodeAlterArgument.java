package com.mcmiddleearth.mcme.pvpplugin.command.argumentTypes;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.util.GameCreator;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ExistingGamemodeAlterArgument implements ArgumentType<String> {

    @Override
    public String parse(StringReader reader) {
        return reader.readUnquotedString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Player source = (Player)context.getSource();
        GameCreator gc = PVPPlugin.getInstance().getGameCreators().get(source.getUniqueId());
        if(gc == null)
            return builder.buildFuture();
        JSONMap map = gc.getMap();
        if(map == null)
            return builder.buildFuture();
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
