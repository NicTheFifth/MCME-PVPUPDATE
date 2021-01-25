package com.mcmiddleearth.mcme.pvpplugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mcmiddleearth.mcme.pvpplugin.Util.Style;
import com.mcmiddleearth.mcme.pvpplugin.Maps.Map;
import com.mojang.brigadier.CommandDispatcher;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PVPPlugin extends JavaPlugin{
    private static boolean debug = true;
    @Getter
    private static PVPPlugin plugin;
    private String spawnWorld;
    @Getter
    private final String fileSep = System.getProperty("file.separator");
    @Getter
    private final ArrayList<String> noHunger = new ArrayList<>();
    @Getter
    private Server serverInstance;
    private CommandDispatcher<Player> commandDispatcher;
    @Getter
    private static Location Spawn;
    //Hashmap of <abbreviation map, map>
    @Getter
    private HashMap<String, Map> maps;
    @Getter
    private Integer bc_min;
    @Getter
    private File pluginDirectory;
    @Getter
    private File playerDirectory;
    @Getter
    private File mapDirectory;
    @Getter
    private File statDirectory;

    @Override
    public void onEnable(){
        //TODO: Add setup of plugin
        plugin = this;
        this.saveDefaultConfig();
        this.reloadConfig();
        if(this.getConfig().contains("worlds")){
            for(String s : this.getConfig().getStringList("worlds")){
                Bukkit.getServer().getWorlds().add(Bukkit.getServer().createWorld(new WorldCreator(s)));
            }
        }
        if(this.getConfig().contains("Broadcast_minutes")){
            bc_min = this.getConfig().getInt("PVP.Broadcast_minutes");
        }
        else
        {
            Logger.getLogger("Logger").log(Level.WARNING, "Broadcast_minutes missing or incorrect");
            bc_min = 2;
        }
        if(this.getConfig().contains("noHunger")){
            noHunger.addAll(this.getConfig().getStringList("noHunger"));
        }
        this.serverInstance = getServer();
        this.pluginDirectory = getDataFolder();
        if (!pluginDirectory.exists()){
            pluginDirectory.mkdir();
        }
        this.playerDirectory = new File(pluginDirectory + this.fileSep + "players");
        if (!playerDirectory.exists()){
            playerDirectory.mkdir();
        }
        this.mapDirectory = new File(pluginDirectory + this.fileSep + "maps");
        if (!mapDirectory.exists()){
            mapDirectory.mkdir();
        }
        this.statDirectory = new File(pluginDirectory + this.fileSep + "stats");
        if (!statDirectory.exists()){
            statDirectory.mkdir();
        }
        PluginManager pm = this.serverInstance.getPluginManager();
        pm.registerEvents(new com.mcmiddleearth.mcme.pvpplugin.Handlers.ArrowHandler(), this);
        Logger.getLogger("PVPPlugin").log(Level.INFO,"PVPPlugin loaded correctly");
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
