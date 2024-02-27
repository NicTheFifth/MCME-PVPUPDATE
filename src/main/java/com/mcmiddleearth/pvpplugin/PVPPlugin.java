package com.mcmiddleearth.pvpplugin;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.command.commandParser.GameCommand;
import com.mcmiddleearth.pvpplugin.command.commandParser.MapEditCommand;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.Playerstat;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.util.*;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PVPPlugin extends JavaPlugin {

    PluginManager pluginManager;
    HandlerList handlerList;
    HashMap<String, JSONMap> maps = new HashMap<>();
    HashMap<UUID, Playerstat> playerstats = new HashMap<>();
    final Location spawn = new Location(Bukkit.getWorld("world"), 344.47, 39, 521.58, 0.3F, -24.15F);
    Matchmaker matchmaker;
    GamemodeRunner activeGame;
    Queue<GamemodeRunner> gameQueue = new LinkedList<>();
    HashMap<UUID, MapEditor> mapEditors = new HashMap<>();
    static PVPPlugin instance;
    //TODO: Implement switching between servermode and minigame mode.
    Boolean isPVPServer = true;
    File mapDirectory;
    File statDirectory;

    @Override
    public void onEnable() {
        if(PVPPlugin.instance == null) {
            PVPPlugin.instance = this;
        }
        this.saveDefaultConfig();
        this.reloadConfig();
        /*if(this.getConfig().contains("noHunger")){
            noHunger.addAll(this.getConfig().getStringList("noHunger"));
        }*/
        if(this.getConfig().contains("worlds")){
            for(String s : this.getConfig().getStringList("worlds")){
                Bukkit.getServer().getWorlds().add(Bukkit.getServer().createWorld(new WorldCreator(s)));
            }
        }
        if (!getDataFolder().mkdir()) {
            if (!getDataFolder().exists()) {
                Logger.getLogger("PVPPlugin").log(Level.SEVERE, "Data folder doesn't exist and wasn't able to be created");
            }
        }
        mapDirectory = new File(getDataFolder() + System.getProperty("file.separator") + "maps");
        if (!mapDirectory.mkdir()) {
            if (!mapDirectory.exists()) {
                Logger.getLogger("PVPPlugin").log(Level.SEVERE, "Map directory doesn't exist and wasn't able to be created");
            }
        }
        statDirectory = new File(getDataFolder() + System.getProperty("file.separator") + "stats");
        if (!statDirectory.mkdir()) {
            if (!statDirectory.exists()) {
                Logger.getLogger("PVPPlugin").log(Level.SEVERE, "Stat directory doesn't exist and wasn't able to be created");
            }
        }
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        setup();
        Logger.getLogger("PVPPlugin").log(Level.INFO, "PVPPlugin loaded correctly");
    }

    private void setup() {
        MapLoader.loadMaps();
        StatLoader.loadStats();
        pluginManager = this.getServer().getPluginManager();
        handlerList = new HandlerList();
        matchmaker = new Matchmaker();
        MapEditCommand mapEditCommand = new MapEditCommand("mapedit");
        Bukkit.getServer().getPluginCommand("mapedit").setExecutor(mapEditCommand);
        Bukkit.getServer().getPluginCommand("mapedit").setTabCompleter(mapEditCommand);
        GameCommand gameCommand = new GameCommand("pvp");
        Bukkit.getServer().getPluginCommand("pvp").setExecutor(gameCommand);
        Bukkit.getServer().getPluginCommand("pvp").setTabCompleter(gameCommand);
        addEventListener(new GlobalListeners());
    }

    @Override
    public void onDisable() {
        if(activeGame != null)
            activeGame.end(true);
        MapLoader.saveMaps();
        StatLoader.saveStats();
    }

    public static void sendInfo(CommandSender recipient, ComponentBuilder message) {
        ComponentBuilder result = new ComponentBuilder("[PVPPlugin]").color(Style.INFO_STRESSED).append(" ").color(Style.INFO);
        result.append(message.create());
        recipient.sendMessage(result.create());
    }

    public static void sendError(CommandSender recipient, ComponentBuilder message) {
        ComponentBuilder result = new ComponentBuilder("[PVPPlugin]").color(Style.INFO_STRESSED).append(" ").color(Style.ERROR);
        result.append(message.create());
        recipient.sendMessage(result.create());
    }

    public static void addEventListener(Listener listener){
        PVPPlugin pvpPlugin = PVPPlugin.getInstance();
        PluginManager pm = pvpPlugin.getPluginManager();
        pm.registerEvents(listener, pvpPlugin);
    }
    public static void removeEventListener(Listener listener){
        PVPPlugin pvpPlugin = PVPPlugin.getInstance();
        pvpPlugin.getHandlerList().unregister(listener);

    }
    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public WorldEditPlugin getWorldEditPlugin(){
        Plugin p = pluginManager.getPlugin("WorldEdit");

        if(p == null){
            return null;
        }
        return (WorldEditPlugin) p;
    }
    public static PVPPlugin getInstance() {
        return instance;
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }
    public HandlerList getHandlerList(){ return handlerList;
    }
    public HashMap<String, JSONMap> getMaps() {
        return this.maps;
    }

    public HashMap<UUID, Playerstat> getPlayerstats() {
        return this.playerstats;
    }
    public GamemodeRunner getActiveGame() {
        return this.activeGame;
    }

    public Queue<GamemodeRunner> getGameQueue() {
        return gameQueue;
    }

    public void setActiveGame(final GamemodeRunner activeGame) {
        this.activeGame = activeGame;
    }
    public File getMapDirectory() {
        return this.mapDirectory;
    }
    public File getStatDirectory() {
        return this.statDirectory;
    }
    public HashMap<UUID,MapEditor> getMapEditors(){return this.mapEditors;}
    public Location getSpawn(){
        return spawn;
    }
    public Boolean isPVPServer(){
        return isPVPServer;
    }

    //</editor-fold>
    private static class GlobalListeners implements Listener{
        @EventHandler
        public void onJoinEvent(PlayerJoinEvent e){
            Player p = e.getPlayer();
            HashMap<UUID, Playerstat> playerStats =
                getInstance().getPlayerstats();
            if(!playerStats.containsKey(p.getUniqueId()))
                playerStats.put(p.getUniqueId(), new Playerstat());
        }
    }
}
