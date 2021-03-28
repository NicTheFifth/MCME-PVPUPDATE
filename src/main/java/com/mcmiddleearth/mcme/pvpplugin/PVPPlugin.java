package com.mcmiddleearth.mcme.pvpplugin;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mcmiddleearth.mcme.pvpplugin.Handlers.*;
import com.mcmiddleearth.mcme.pvpplugin.Maps.MapManager;
import com.mcmiddleearth.mcme.pvpplugin.PVP.PlayerStat;
import com.mcmiddleearth.mcme.pvpplugin.Util.ShortEventClass;
import com.mcmiddleearth.mcme.pvpplugin.Util.Style;
import com.mcmiddleearth.mcme.pvpplugin.command.PVPCommand;
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
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class PVPPlugin extends JavaPlugin {

    @Getter
    private Server serverInstance;

    private CommandDispatcher<Player> commandDispatcher;

    @Getter
    private static Location lobbySpawn;

    private MapManager mapManager;

    @Getter
    private Integer broadcastMinutes;

    @Getter
    private final HashMap<Class<?>, EventRebroadcaster> listenerMap = new HashMap<>();

    private WorldEditPlugin worldEdit;

    @Getter
    private HashMap<UUID, PlayerStat> playerStats = new HashMap<>();

    @Getter
    private File pluginDirectory;
    @Getter
    private File playerDirectory;
    @Getter
    private File mapDirectory;
    @Getter
    private File statDirectory;
    @Getter
    private PluginManager pluginManager;

    @Override
    public void onEnable() {
        this.worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

        loadConfig();
        loadListeners();

        this.mapManager = new MapManager(mapDirectory);

        this.commandDispatcher = new PVPCommand(this)

        Logger.getLogger("PVPPlugin").log(Level.INFO, "PVPPlugin loaded correctly");
    }
    @Override
    public void onDisable() {

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

    private void loadConfig() {
        this.saveDefaultConfig();
        this.reloadConfig();
        if (this.getConfig().contains("worlds")) {
            for (String s : this.getConfig().getStringList("worlds")) {
                Bukkit.getServer().getWorlds().add(Bukkit.getServer().createWorld(new WorldCreator(s)));
            }
        }
        if (this.getConfig().contains("Broadcast_minutes")) {
            broadcastMinutes = this.getConfig().getInt("PVP.Broadcast_minutes");
        } else {
            Logger.getLogger("Logger").log(Level.WARNING, "Broadcast_minutes missing or incorrect");
            broadcastMinutes = 2;
        }
        this.serverInstance = getServer();
        this.pluginDirectory = getDataFolder();
        this.pluginDirectory.mkdir();

        this.playerDirectory = new File(String.format("%s/players", pluginDirectory));
        this.playerDirectory.mkdir();

        this.mapDirectory = new File(String.format("%s/maps", pluginDirectory));
        this.mapDirectory.mkdir();

        this.statDirectory = new File(String.format("%s/stats", pluginDirectory));
        this.statDirectory.mkdir();
    }

    private void loadListeners() {
        this.pluginManager = this.serverInstance.getPluginManager();

        listenerMap.put(ShortEventClass.ARROW_GRAB, new OnArrowPickupRebroadcaster());
        listenerMap.put(ShortEventClass.SHOOT, new OnArrowShootRebroadcaster(this));
        listenerMap.put(ShortEventClass.PLAYER_DEATH, new OnDeathRebroadcaster());
        listenerMap.put(ShortEventClass.PLAYER_INTERACT, new OnPlayerInteractRebroadcaster());
        listenerMap.put(ShortEventClass.BLOCK_DAMAGE, new OnPlayerBlockDamageRebroadcaster());
        listenerMap.put(ShortEventClass.PLAYER_COMMAND, new OnCommandRebroadcaster());
        listenerMap.put(ShortEventClass.PLAYER_MOVE, new OnPlayerMoveRebroadcaster());
        listenerMap.put(ShortEventClass.RESPAWN, new OnRespawnRebroadcaster());

        listenerMap.values().forEach(listener -> pluginManager.registerEvents(listener, this));
    }
}
