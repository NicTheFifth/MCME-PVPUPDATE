package com.mcmiddleearth.mcme.pvpplugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.Playerstat;
import com.mcmiddleearth.mcme.pvpplugin.runners.GamemodeRunner;
import com.mcmiddleearth.mcme.pvpplugin.util.Matchmaker;
import com.mcmiddleearth.mcme.pvpplugin.util.Style;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
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

    private File mapDirectory;

    @Override
    public void onLoad(){
        this.saveDefaultConfig();
        this.reloadConfig();
        if(this.getConfig().contains("noHunger")){
            noHunger.addAll(this.getConfig().getStringList("noHunger"));
        }
        CLog.println(getDataFolder().getPath());
        if (!getDataFolder().exists()){
            getDataFolder().mkdir();
        }
        mapDirectory = new File(getDataFolder() + System.getProperty("file.separator") + "maps");
        if (!mapDirectory.exists()){
            mapDirectory.mkdir();
        }
        this.statDirectory = new File(getDataFolder() + System.getProperty("file.separator") + "stats");
        if (!statDirectory.exists()){
            statDirectory.mkdir();
        }
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            //PlaceholderAPI.registerPlaceholderExpansion("mcmePvP", new com.mcmiddleearth.mcme.pvp.Handlers.ChatHandler());
            new com.mcmiddleearth.mcme.pvp.Handlers.ChatHandler().register();
        } else {
            Logger.getGlobal().warning("PlaceholderAPI not enabled");
        }
        HashMap<String, Object> maps = new HashMap<>();
        try{
            maps = DBmanager.loadAllObj(Map.class, this.mapDirectory);
        }
        catch(Exception ex){
        }
        if(maps == null){
            maps = new HashMap<>();
        }
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
