package com.mcmiddleearth.mcme.pvpplugin.command.handler;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.Util.Permission;
import com.mcmiddleearth.mcme.pvpplugin.command.argument.OnlinePlayerArgumentType;
import com.mcmiddleearth.mcme.pvpplugin.command.builder.HelpfulLiteralBuilder;
import com.mojang.brigadier.CommandDispatcher;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.security.Permissions;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LockerCommandHandler extends AbstractCommandHandler{

    @Setter@Getter
    public boolean locked;

    //TODO: Make Locker command Handler
    public LockerCommandHandler (String CommandName, CommandDispatcher<CommandSender> dispatcher){
        super(CommandName);
        dispatcher
                .register(HelpfulLiteralBuilder.literal(CommandName)
                        .requires(commandSender -> commandSender.hasPermission(Permission.PVP_MANAGER))
                        .then(HelpfulLiteralBuilder.literal("lock")
                            .executes(command -> {
                                toggleLock(command.getSource(),locked);
                                return 0;
                            }))
                        .then(HelpfulLiteralBuilder.literal("kickall")
                                .executes(command -> {
                                    KickAll(command.getSource());
                                    return 0;
                                }))
                        .then(HelpfulLiteralBuilder.literal("kick")
                                .executes(command -> {
                                    KickPlayer(new OnlinePlayerArgumentType(), command.getSource(), command.getInput());
                                    return 0;
                                }))
                        .then(HelpfulLiteralBuilder.literal("ban")
                                .executes(command -> {
                                    BanPlayer(new OnlinePlayerArgumentType(),command.getSource());
                                    return 0;
                                }))
                        .then(HelpfulLiteralBuilder.literal("pardon")
                                .executes(command -> {
                                    PardonPlayer( new OnlinePlayerArgumentType(), command.getSource());
                                    return 0;
                                }))
                );
    }

    private void toggleLock(CommandSender source, boolean locked){//move this locked boolean to the start so it is set to false when server starts up and shuts down
        if(locked){
            source.sendMessage("Server Unlocked!");
            locked = false;
        }
        else {
            source.sendMessage("Server Locked!");
            locked = true;
            String Message = "Server Locked!";
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.hasPermission(Permission.PVP_JOIN)) {
                    p.sendMessage(Message);
                    sendPlayerToMain(p);
                }
            }
        }
    }
    private void KickAll(CommandSender source){
        for(Player p : Bukkit.getOnlinePlayers()){
            if(!p.hasPermission(Permission.PVP_JOIN)){
                p.sendMessage("A PvP manager kicked all players");
                sendPlayerToMain(p);
            }
        }
        source.sendMessage("All players kicked!");
    }
    private void KickPlayer(OnlinePlayerArgumentType playername, CommandSender source, String argument){
        Logger.getLogger("logger").log(Level.INFO, "kickPlayer received with " + argument);
    }
    private void BanPlayer(OnlinePlayerArgumentType playername, CommandSender source){
    }
    private void PardonPlayer(OnlinePlayerArgumentType playername , CommandSender source){
    }
    private void sendPlayerToMain(Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(player.getName());
        out.writeUTF("world");
        player.sendPluginMessage(PVPPlugin.getPlugin(), "BungeeCord", out.toByteArray());
    }
}

