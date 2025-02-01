package com.mcmiddleearth.pvpplugin;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.function.Function;

import com.mcmiddleearth.pvpplugin.command.commandParser.GameCommand;
import com.mcmiddleearth.pvpplugin.command.commandParser.MapEditCommand;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.Playerstat;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.util.*;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PVPPlugin extends JavaPlugin {

    MiniMessage mm = MiniMessage.miniMessage();
    PluginManager pluginManager;
    private Audience adventure;
    HandlerList handlerList;
    HashMap<String, JSONMap> maps = new HashMap<>();
    HashMap<UUID, Playerstat> playerstats = new HashMap<>();
    Set<Player> autojoiners = new HashSet<>();
    Location spawn;
    Matchmaker matchmaker;
    GamemodeRunner activeGame;
    Queue<Supplier<GamemodeRunner>> gameQueue = new LinkedList<>();
    HashMap<UUID, MapEditor> mapEditors = new HashMap<>();
    static PVPPlugin instance;
    //TODO: Implement switching between servermode and minigame mode.
//    Boolean isPVPServer = true;
    File mapDirectory;
    File statDirectory;

    @Override
    public void onEnable() {
        if(PVPPlugin.instance == null) {
            PVPPlugin.instance = this;
        }
        this.saveDefaultConfig();
        this.reloadConfig();
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
        mapDirectory = new File(getDataFolder() + FileSystems.getDefault().getSeparator() + "maps");
        if (!mapDirectory.mkdir()) {
            if (!mapDirectory.exists()) {
                Logger.getLogger("PVPPlugin").log(Level.SEVERE, "Map directory doesn't exist and wasn't able to be created");
            }
        }
        statDirectory = new File(getDataFolder() + FileSystems.getDefault().getSeparator() + "stats");
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
        this.adventure = Audience.audience(Bukkit.getServer());
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
        spawn = new Location(Bukkit.getWorld("world"), 344.47, 39, 521.58,
                0.3F, -24.15F);
    }

    @Override
    public void onDisable() {
        if(activeGame != null)
            activeGame.end(true);
        MapLoader.saveMaps();
        StatLoader.saveStats();
        if(this.adventure != null) {
            this.adventure = null;
        }
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
    public MiniMessage getMiniMessage(){
        return mm;
    }

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
    public Set<Player> getAutojoiners(){return autojoiners;}
    public HashMap<String, JSONMap> getMaps() {
        return this.maps;
    }

    public HashMap<UUID, Playerstat> getPlayerstats() {
        return this.playerstats;
    }
    public GamemodeRunner getActiveGame() {
        return this.activeGame;
    }

    public Queue<Supplier<GamemodeRunner>> getGameQueue() {
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
//    public Boolean isPVPServer(){
//        return isPVPServer;
//    }

    //</editor-fold>
    public void sendMessage(Component message){
        adventure.filterAudience(audience -> audience instanceof Player).sendMessage(message);
    }

    public void sendMessage(String message) {
        adventure.filterAudience(audience -> audience instanceof Player).sendMessage(mm.deserialize(message));
    }

    public void sendMessageTo(Component message, Player... player){
        if(player.length == 0)
            Logger.getLogger("MCME-PVP").log(Level.INFO, "Tried to send a message to an empty list of players.");
        sendMessageTo(message, Arrays.stream(player).collect(Collectors.toSet()));
    }
    public void sendMessageTo(Component message, Set<Player> player){
            Audience subAud = adventure.filterAudience(member -> member instanceof Player && player.contains(member));
            subAud.sendMessage(message);
        }

    public void sendMessageTo(String message, Player... player) {
        if (player.length == 0)
            Logger.getLogger("MCME-PVP").log(Level.INFO, "Tried to send a message to an empty list of players.");
        sendMessageTo(message, Arrays.stream(player).collect(Collectors.toSet()));
    }

    public void sendMessageTo(String message, Set<Player> player){
        Audience subAud = adventure.filterAudience(member -> member instanceof Player && player.contains(member));
        subAud.sendMessage(Component.text(message));
    }

    private static class GlobalListeners implements Listener{
        MiniMessage mm = MiniMessage.miniMessage();
        @EventHandler
        public void onChat(AsyncChatEvent e){
            PVPPlugin pvpPlugin = PVPPlugin.getInstance();
            Player player = e.getPlayer();
            GamemodeRunner runner = pvpPlugin.getActiveGame();
            String placeholder = "<<color>><prefix> <name></<color>>: <message>";
            TagResolver.Single message = Placeholder.component("message", e.message());
            TagResolver.Single name = Placeholder.parsed("name", player.getName());

            if(runner == null || runner.getGameState() == GamemodeRunner.State.QUEUED){
                if(player.hasPermission(Permissions.PVP_ADMIN.getPermissionNode())){
                    pvpPlugin.sendMessage(mm.deserialize(
                            placeholder,
                            message,
                            name,
                            Placeholder.parsed("color", "gold"),
                            Placeholder.parsed("prefix", "PVP Staff")));
                    e.setCancelled(true);
                    return;
                }
                if(player.hasPermission(Permissions.RUN.getPermissionNode())){
                    pvpPlugin.sendMessage(mm.deserialize(
                            placeholder,
                            message,
                            name,
                            Placeholder.parsed("color", "gold"),
                            Placeholder.parsed("prefix", "Manager")));
                    e.setCancelled(true);
                    return;
                }
                pvpPlugin.sendMessage(mm.deserialize(
                        placeholder,
                        message,
                        name,
                        Placeholder.parsed("color", "gray"),
                        Placeholder.parsed("prefix", "Lobby")));
                e.setCancelled(true);
                return;
            }
            Function<List<TagResolver>, Component> messageBuilder = (List<TagResolver> resolvers) -> {
                resolvers.add(message);
                resolvers.add(name);
                return mm.deserialize(
                        placeholder,
                        TagResolver.builder().resolvers(resolvers).build());
            };
            if(runner.trySendSpectatorMessage(player, messageBuilder)){
                e.setCancelled(true);
                return;
            }
            e.setCancelled(runner.trySendMessage(player, messageBuilder));
        }

        @EventHandler
        public void onJoinEvent(PlayerJoinEvent e){
            Player p = e.getPlayer();
            HashMap<UUID, Playerstat> playerStats =
                PVPPlugin.getInstance().getPlayerstats();
            if(!playerStats.containsKey(p.getUniqueId()))
                playerStats.put(p.getUniqueId(), new Playerstat());
        }

        @EventHandler
        public void onLeaveEvent(PlayerQuitEvent e) {
             PVPPlugin.getInstance().autojoiners.remove(e.getPlayer());
        }
    }
}
