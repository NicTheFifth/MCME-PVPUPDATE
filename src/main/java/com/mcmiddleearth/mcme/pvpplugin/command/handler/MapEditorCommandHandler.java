package com.mcmiddleearth.mcme.pvpplugin.command.handler;

import com.mcmiddleearth.mcme.pvpplugin.Util.Permission;
import com.mcmiddleearth.mcme.pvpplugin.command.builder.HelpfulLiteralBuilder;
import com.mojang.brigadier.CommandDispatcher;
import net.md_5.bungee.api.CommandSender;

public class MapEditorCommandHandler extends AbstractCommandHandler{
    //TODO: Make the full command tree
    public MapEditorCommandHandler (String name, CommandDispatcher<CommandSender> dispatcher){
        super(name);
        dispatcher
            .register(HelpfulLiteralBuilder.literal(name)
                .withHelpText("Use this to create/edit new maps!")
                .withTooltip("Usage is to create new maps or edit existing maps!")
                .requires(commandSender -> commandSender.hasPermission(Permission.MAP_EDITOR))
                .then(HelpfulLiteralBuilder.literal("create")
                    .withHelpText("Create a new map!")
                    .withTooltip("Put in a name to create a new map!")
                )
            );
    }
}
