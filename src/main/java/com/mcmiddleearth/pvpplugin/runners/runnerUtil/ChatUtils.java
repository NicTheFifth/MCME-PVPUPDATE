package com.mcmiddleearth.pvpplugin.runners.runnerUtil;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;

public class ChatUtils {
    public static void sendBroadcast(BaseComponent[] message){
//        PVPPlugin.getInstance().getServer().getOnlinePlayers().forEach( player ->
//                sendBaseComponent(message, player)
//        );
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Message");
        out.writeUTF("ALL");
        out.writeUTF(BaseComponent.toLegacyText(message));
        ((Player)PVPPlugin.getInstance().getServer().getOnlinePlayers().toArray()[0]).sendPluginMessage(PVPPlugin.getInstance(), "BungeeCord", out.toByteArray());
    }

    public static void AnnounceNewGame(String gamemode, String map, String max){
        ChatUtils.sendBroadcast(new ComponentBuilder("A new game was started!\n").color(ChatColor.GRAY)
                .append(ChatUtils.GreenChat(gamemode)).append(ChatUtils.GrayChat(" on ")).append(ChatUtils.GreenChat(map))
                .append(ChatUtils.GrayChat("\nUse ")).append(ChatUtils.GreenChat("/pvp join ")).append(ChatUtils.GrayChat("to join the game"))
                .append(ChatUtils.GrayChat("\nThere are only ")).append(ChatUtils.GrayChat(max)).append(ChatUtils.GrayChat(" places!"))
                .append(ChatUtils.GrayChat("\nDo ")).append(ChatUtils.GreenChat("/pvp rules")).append(ChatUtils.GrayChat(" if you don't know how this gamemode works.")).create());
    }

    public static BaseComponent[] GreenChat(String message){
        return new ComponentBuilder(message).color(ChatColor.GREEN).create();
    }

    public static BaseComponent[] GrayChat(String message){
        return new ComponentBuilder(message).color(ChatColor.GRAY).create();
    }
}
