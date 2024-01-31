package com.mcmiddleearth.pvpplugin.command.argumentTypes;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.GamemodeEditor;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.entity.Player;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SpawnArgument implements ArgumentType<String> {
    Set<String> options;
    public SpawnArgument(String... options){
        this.options = Set.of(options);
    }
    @Override
    public String parse(StringReader reader) throws CommandSyntaxException{
        String o = reader.readUnquotedString();
        if(!options.contains(o)){
            LiteralMessage message = new LiteralMessage(
                o + " isn't a valid spawn name, please use one of the valid " +
                    "spawn names.");
            throw new CommandSyntaxException(
                new SimpleCommandExceptionType(message), message);
        }
        return o;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        Player source = CommandUtil.getPlayer((McmeCommandSender) context.getSource());
        if(source == null) {
            return builder.buildFuture();
        }
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(source.getUniqueId());
        if(me == null) {
            return builder.buildFuture();
        }
        GamemodeEditor gamemodeEditor = me.getGamemodeEditor();
        if(gamemodeEditor == null) {
            return builder.buildFuture();
        }
//
//        if(Objects.equals(map.getGamemode(), "Free For All"))
//            options.add("spawn");
//
//        if(Objects.equals(map.getGamemode(), "Deathrun")){
//            options.add("death");
//            options.add("runner");
//        }
//        if(Objects.equals(map.getGamemode(), "Infected")){
//            options.add("infected");
//            options.add("survivor");
//        }


        for (String option : options) {
            if (option.startsWith(builder.getRemaining())) {
                builder.suggest(option);
            }
        }
        return builder.buildFuture();
    }
}
