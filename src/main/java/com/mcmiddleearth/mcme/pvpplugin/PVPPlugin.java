package com.mcmiddleearth.mcme.pvpplugin;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.Playerstat;
import com.mcmiddleearth.mcme.pvpplugin.runners.GamemodeRunner;
import com.mcmiddleearth.mcme.pvpplugin.util.MapLoader;
import com.mcmiddleearth.mcme.pvpplugin.util.Matchmaker;
import com.mcmiddleearth.mcme.pvpplugin.util.StatLoader;
import com.mcmiddleearth.mcme.pvpplugin.util.Style;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PVPPlugin extends JavaPlugin {

    @Getter
    PluginManager pluginManager;

    @Getter
    HashMap<String, JSONMap> maps;

    @Getter
    HashMap<UUID, Playerstat> playerstats;

    @Getter
    Matchmaker matchmaker;

    @Getter@Setter
    GamemodeRunner activeGame;
    @Getter
    MapLoader mapLoader;
    @Getter
    StatLoader statLoader;

    @Getter
    private File mapDirectory;
    @Getter
    private File statDirectory;

    @Override
    public void onLoad(){
        this.saveDefaultConfig();
        this.reloadConfig();
        mapLoader = new MapLoader(this);
        statLoader = new StatLoader(this);
        /*if(this.getConfig().contains("noHunger")){
            noHunger.addAll(this.getConfig().getStringList("noHunger"));
        }*/

        if (!getDataFolder().mkdir()){
            if(!getDataFolder().exists()){
                Logger.getLogger("PVPPlugin").log(Level.SEVERE, "Data folder doesn't exist and wasn't able to be created");
            }
        }
        mapDirectory = new File(getDataFolder() + System.getProperty("file.separator") + "maps");
        if (!mapDirectory.mkdir()){
            if(!mapDirectory.exists()){
                Logger.getLogger("PVPPlugin").log(Level.SEVERE, "Map directory doesn't exist and wasn't able to be created");
            }
        }
        statDirectory = new File(getDataFolder() + System.getProperty("file.separator") + "stats");
        if (!statDirectory.mkdir()){
            if(!statDirectory.exists()){
                Logger.getLogger("PVPPlugin").log(Level.SEVERE, "Stat directory doesn't exist and wasn't able to be created");
            }
        }
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        mapLoader.loadMaps();
        statLoader.loadStats();
        //Spawn = new Location(Bukkit.getWorld("world"), 344.47, 39, 521.58, 0.3F, -24.15F);

        Logger.getLogger("PVPPlugin").log(Level.INFO, "PVPPlugin loaded correctly");
    }

    @Override
    public void onEnable() {
        setup();

        Logger.getLogger("PVPPlugin").log(Level.INFO, "PVPPlugin enabled correctly");
    }

    private void setup() {
        pluginManager = this.getServer().getPluginManager();
        matchmaker = new Matchmaker(this);
    }

    @Override
    public void onDisable() {
        mapLoader.saveMaps();
        statLoader.saveStats();
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
