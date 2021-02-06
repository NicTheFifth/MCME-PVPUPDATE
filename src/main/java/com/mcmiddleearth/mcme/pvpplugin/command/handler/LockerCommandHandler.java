package com.mcmiddleearth.mcme.pvpplugin.command.handler;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.Util.Permission;
import com.mcmiddleearth.mcme.pvpplugin.command.argument.OnlinePlayerArgumentType;
import com.mcmiddleearth.mcme.pvpplugin.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.mcme.pvpplugin.command.builder.HelpfulRequiredArgumentBuilder;
import com.mojang.brigadier.CommandDispatcher;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
                            .then(HelpfulRequiredArgumentBuilder.argument("kickedPlayer", new OnlinePlayerArgumentType())
                                .executes(command -> {
                                    KickPlayer(command.getSource(), command.getArgument("kickedPlayer", String.class));
                                    return 0;
                                })))
                        .then(HelpfulLiteralBuilder.literal("ban")
                                .then(HelpfulRequiredArgumentBuilder.argument("bannedPlayer", new OnlinePlayerArgumentType())
                                .executes(command -> {
                                    BanPlayer(command.getArgument("bannedPlayer", String.class),command.getSource());
                                    return 0;
                                })))
                        .then(HelpfulLiteralBuilder.literal("pardon")
                                .then(HelpfulRequiredArgumentBuilder.argument("pardonedPlayer", new OnlinePlayerArgumentType())
                                .executes(command -> {
                                    PardonPlayer(command.getArgument("pardonedPlayer", String.class), command.getSource());
                                    return 0;
                                })))
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
    private void KickPlayer(CommandSender source, String playerToKick){
        Player kickPlayer = Bukkit.getPlayer(playerToKick);
        kickPlayer.sendMessage("You have been kicked from PVP.");
        sendPlayerToMain(kickPlayer);
        source.sendMessage(playerToKick + " has been kicked.");
    }
    private void BanPlayer(String playerToBan, CommandSender source){
        Player kickPlayer = Bukkit.getPlayer(playerToBan);
        kickPlayer.sendMessage("You have been kicked from PVP.");
        sendPlayerToMain(kickPlayer);
        source.sendMessage(playerToBan + " has been kicked.");
    }
    private void PardonPlayer(String playerToPardon , CommandSender source){
        Player kickPlayer = Bukkit.getPlayer(playerToPardon);
        kickPlayer.sendMessage("You have been kicked from PVP.");
        sendPlayerToMain(kickPlayer);
        source.sendMessage(playerToPardon + " has been kicked.");
    }
    private void sendPlayerToMain(Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(player.getName());
        out.writeUTF("world");
        player.sendPluginMessage(PVPPlugin.getPlugin(), "BungeeCord", out.toByteArray());
    }
}

