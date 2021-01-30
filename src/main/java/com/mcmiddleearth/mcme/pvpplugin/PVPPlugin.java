package com.mcmiddleearth.mcme.pvpplugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.mcmiddleearth.mcme.pvpplugin.Handlers.*;
import com.mcmiddleearth.mcme.pvpplugin.Util.Style;
import com.mcmiddleearth.mcme.pvpplugin.Maps.Map;
import com.mojang.brigadier.CommandDispatcher;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class PVPPlugin extends JavaPlugin{
    @Getter @Setter
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
    private WorldEditPlugin worldEdit;
    @Getter
    private HashMap<Class<?>, EventRebroadcaster> listenerMap;

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
        worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        PluginManager pm = this.serverInstance.getPluginManager();

        listenerMap.put(PlayerPickupArrowEvent.class, new OnArrowPickupRebroadcaster());
        listenerMap.put(EntityShootBowEvent.class, new OnArrowShootRebroadcaster());
        listenerMap.put(PlayerDeathEvent.class, new OnDeathRebroadcaster());
        listenerMap.put(PlayerInteractEvent.class, new OnPlayerInteractRebroadcaster());
        listenerMap.put(EntityDamageByBlockEvent.class, new OnPlayerBlockDamageRebroadcaster());

        listenerMap.values().forEach(listener -> pm.registerEvents(listener, this));
        Logger.getLogger("PVPPlugin").log(Level.INFO,"PVPPlugin loaded correctly");
    }

    @Override
    public void onDisable(){

    }

    public <T> void addEventListener(Class<T> eventType, EventListener<T> listener) {
        listenerMap.get(eventType).addListener(listener);
    }

    public <T> void removeEventListener(Class<T> eventType, EventListener<T> listener) {
        listenerMap.get(eventType).removeListener(listener);
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
