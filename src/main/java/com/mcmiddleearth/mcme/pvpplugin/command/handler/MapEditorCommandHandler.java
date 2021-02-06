package com.mcmiddleearth.mcme.pvpplugin.command.handler;

import com.mcmiddleearth.mcme.pvpplugin.Util.Permission;
import com.mcmiddleearth.mcme.pvpplugin.command.argument.*;
import com.mcmiddleearth.mcme.pvpplugin.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.mcme.pvpplugin.command.builder.HelpfulRequiredArgumentBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.md_5.bungee.api.CommandSender;
import org.bukkit.entity.Player;

public class MapEditorCommandHandler extends AbstractCommandHandler{
    //TODO: Make the full command tree
    public MapEditorCommandHandler (String commandName, CommandDispatcher<CommandSender> dispatcher){
        super(commandName);
        dispatcher
            /*.register(HelpfulLiteralBuilder.literal(commandName)
                .withHelpText("Use this to create/edit new maps!")
                .withTooltip("Usage is to create new maps or edit existing maps!")
                .requires(commandSender -> commandSender.hasPermission(Permission.MAP_EDITOR))
                .then(HelpfulLiteralBuilder.literal("create")
                    .withHelpText("Create a new map!")
                    .withTooltip("Put in a name to create a new map!")
                )
            );*/
            .register(HelpfulLiteralBuilder.literal(commandName)
                    .requires(commandSender -> commandSender.hasPermission(Permission.PVP_ADMIN))
                    .then(HelpfulLiteralBuilder.literal("create"))
                    .then(HelpfulRequiredArgumentBuilder.argument("map", new MapAbbreviationArgumentType())
                            .then(HelpfulLiteralBuilder.literal("setarea"))
                            .then(HelpfulLiteralBuilder.literal("settitle"))
                            .then(HelpfulLiteralBuilder.literal("setspawn"))
                            .then(HelpfulLiteralBuilder.literal("setrp"))
                            .then(HelpfulLiteralBuilder.literal("addgamemode"))
                            .then(HelpfulRequiredArgumentBuilder.argument("addgamemode", new GamemodeArgumentType())
                                    .then(HelpfulLiteralBuilder.literal("maxPlayers")
                                        .then(HelpfulRequiredArgumentBuilder.argument("maxPlayers", new IntArgumentType())))
                                    .then(HelpfulLiteralBuilder.literal("spawn")
                                            .then(HelpfulLiteralBuilder.literal("show"))
                                            .then(HelpfulLiteralBuilder.literal("hide"))
                                            .then(HelpfulLiteralBuilder.literal("list"))
                                            .then(HelpfulRequiredArgumentBuilder.argument("set", new SpawnNameArgumentType()))
                                    )
                                    .then(HelpfulRequiredArgumentBuilder.argument("setPOI", new POINameArgumentType()))
                            )
                    )
        );

    }
}
