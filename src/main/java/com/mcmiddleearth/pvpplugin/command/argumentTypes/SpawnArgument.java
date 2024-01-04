package com.mcmiddleearth.pvpplugin.command.argumentTypes;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.GamemodeEditor;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SpawnArgument implements ArgumentType<String> {

    @Override
    public String parse(StringReader reader) {
        return reader.readUnquotedString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        Player source = (Player) context.getSource();
        if(source == null)
            return builder.buildFuture();
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null)
            return builder.buildFuture();
        GamemodeEditor map = me.getGamemodeEditor();
        if(map == null)
            return builder.buildFuture();
        Set<String> options = new java.util.HashSet<>(Collections.emptySet());

        if(Objects.equals(map.getGamemode(), "Capture the Flag") ||
                Objects.equals(map.getGamemode(), "Team Conquest") ||
                Objects.equals(map.getGamemode(), "Team Deathmatch") ||
                Objects.equals(map.getGamemode(), "Ringbearer")) {
            options.add("blue");
            options.add("red");
        }

        if(Objects.equals(map.getGamemode(), "Free For All"))
            options.add("spawn");

        if(Objects.equals(map.getGamemode(), "Deathrun")){
            options.add("death");
            options.add("runner");
        }
        if(Objects.equals(map.getGamemode(), "Infected")){
            options.add("infected");
            options.add("survivor");
        }


        for (String option : options) {
            if (option.startsWith(builder.getRemaining())) {
                builder.suggest(option);
            }
        }
        return builder.buildFuture();
    }
}
