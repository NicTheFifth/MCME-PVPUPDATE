package com.mcmiddleearth.mcme.pvpplugin;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mcmiddleearth.mcme.pvpplugin.command.PVPCommand;
import com.mcmiddleearth.mcme.pvpplugin.util.Style;
import com.mojang.brigadier.CommandDispatcher;
import lombok.Getter;
import com.mojang.brigadier.CommandDispatcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PVPPlugin extends JavaPlugin{
    private static boolean debug = true;
    @Getter
    private static PVPPlugin plugin;
    private String spawnWorld;
    private static String FileSep = System.getProperty("file.separator");
    private ArrayList<String> noHunger = new ArrayList<String>();
    private static Server serverInstance;
    private CommandDispatcher<Player> commandDispatcher;
    @Getter
    private static Location Spawn;


    @Override
    public void onEnable(){
        //TODO: Add setup of plugin
        Logger.getLogger("BasePlugin").log(Level.INFO,"BasePlugin loaded correctly");
        plugin = this;
    }
    @Override
    public void onDisable(){

    }

    public static void sendInfo(CommandSender recipient, ComponentBuilder message) {
        ComponentBuilder result = new ComponentBuilder("[Mod]").color(Style.MOD).append(" ").color(Style.INFO);
        result.append(message.create());
        recipient.sendMessage(result.create());
    }
    public static void sendError(CommandSender recipient, ComponentBuilder message) {
        ComponentBuilder result = new ComponentBuilder("[Mod]").color(Style.MOD).append(" ").color(Style.ERROR);
        result.append(message.create());
        recipient.sendMessage(result.create());
    }
}
