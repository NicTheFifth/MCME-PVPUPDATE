package com.mcmiddleearth.mcme.pvpplugin.command.handler;

import com.mcmiddleearth.mcme.pvpplugin.Util.Permission;
import com.mcmiddleearth.mcme.pvpplugin.command.builder.HelpfulLiteralBuilder;
import com.mojang.brigadier.CommandDispatcher;
import net.md_5.bungee.api.CommandSender;

public class LockerCommandHandler {
    //TODO: Make Locker command Handler
    public LockerCommandHandler (CommandDispatcher<CommandSender> dispatcher){
        dispatcher
                .register(
                    .requires(commandSender -> commandSender.hasPermission(Permission.PVP_MANAGER))
                    .then()
        );
    }
}
