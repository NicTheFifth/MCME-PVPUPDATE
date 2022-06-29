package com.mcmiddleearth.mcme.pvpplugin;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.mcme.pvpplugin.command.commandParser.GameCommand;
import com.mcmiddleearth.mcme.pvpplugin.command.commandParser.MapEditCommand;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.Playerstat;
import com.mcmiddleearth.mcme.pvpplugin.runners.GamemodeRunner;
import com.mcmiddleearth.mcme.pvpplugin.util.*;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PVPPlugin extends JavaPlugin {

    PluginManager pluginManager;
    HashMap<String, JSONMap> maps = new HashMap<>();
    HashMap<UUID, Playerstat> playerstats = new HashMap<>();
    HashSet<String> availableGamemodes = new HashSet<>(
            Set.of("capturetheflag",
                    "deathrun",
                    "infected",
                    "teamconquest",
                    "teamdeathmatch",
                    "teamslayer",
                    "oneinthequiver",
                    "ringbearer"));
    Matchmaker matchmaker;
    GamemodeRunner activeGame;

    HashMap<UUID, MapEditor> mapEditors = new HashMap<>();
    HashMap<UUID, GameCreator> gameCreators = new HashMap<>();

    private static PVPPlugin instance;
    private File mapDirectory;
    private File statDirectory;

    @Override
    public void onLoad() {
        if(PVPPlugin.instance == null) {
            PVPPlugin.instance = this;
        }
        this.saveDefaultConfig();
        this.reloadConfig();
        /*if(this.getConfig().contains("noHunger")){
            noHunger.addAll(this.getConfig().getStringList("noHunger"));
        }*/
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
        //Spawn = new Location(Bukkit.getWorld("world"), 344.47, 39, 521.58, 0.3F, -24.15F);
        Logger.getLogger("PVPPlugin").log(Level.INFO, "PVPPlugin loaded correctly");
    }

    @Override
    public void onEnable() {
        setup();
        Logger.getLogger("PVPPlugin").log(Level.INFO, "PVPPlugin enabled correctly");
    }

    private void setup() {
        MapLoader.loadMaps();
        StatLoader.loadStats();
        pluginManager = this.getServer().getPluginManager();
        matchmaker = new Matchmaker();
        MapEditCommand mapEditCommand = new MapEditCommand("mapedit");
        Bukkit.getServer().getPluginCommand("mapedit").setExecutor(mapEditCommand);
        Bukkit.getServer().getPluginCommand("mapedit").setTabCompleter(mapEditCommand);
        GameCommand gameCommand = new GameCommand("pvp");
        Bukkit.getServer().getPluginCommand("pvp").setExecutor(gameCommand);
        Bukkit.getServer().getPluginCommand("pvp").setTabCompleter(gameCommand);
    }

    @Override
    public void onDisable() {
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

    public HashMap<String, JSONMap> getMaps() {
        return this.maps;
    }

    public HashMap<UUID, Playerstat> getPlayerstats() {
        return this.playerstats;
    }

    public HashSet<String> getAvailableGamemodes() {
        return this.availableGamemodes;
    }

    public Matchmaker getMatchmaker() {
        return this.matchmaker;
    }

    public GamemodeRunner getActiveGame() {
        return this.activeGame;
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

    public HashMap<UUID, GameCreator> getGameCreators() {
        return gameCreators;
    }

    //</editor-fold>
}
