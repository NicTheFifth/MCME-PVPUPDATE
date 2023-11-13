package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.exceptions.BadRegionException;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.Playerstat;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamDeathMatch;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.GamemodeListener;
import com.mcmiddleearth.pvpplugin.runners.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.KitEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.pvpplugin.util.Kit;
import com.mcmiddleearth.pvpplugin.util.Matchmaker;
import com.mcmiddleearth.pvpplugin.util.Team;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TeamDeathmatchRunner implements GamemodeRunner {
    //TODO: Add team assignment prior to start
    private enum State{
        QUEUED, COUNTDOWN, RUNNING, ENDED
    }
    int maxPlayers;
    State gameState;
    private long countDownTimer = 5;
    Boolean isPrivate;
    Set<Player> whiteList = new HashSet<>();
    Set<Player> players = new HashSet<>();
    TDMTeam red = new TDMTeam();
    TDMTeam blue = new TDMTeam();
    Team spectator = new Team();
    Region region;
    Scoreboard scoreboard;

    //<editor-fold defaultstate="collapsed" desc="Initialisation">
    public TeamDeathmatchRunner(JSONMap map, Boolean isPrivate){
        //TODO: add a check if this game can actually be ran
        JSONTeamDeathMatch tdm = map.getJSONTeamDeathMatch();
        createTeams(tdm);
        createSpectator(map.getSpawn());
        setRegion(map.getRegionPoints(), map.getTitle());
        this.maxPlayers = tdm.getMaximumPlayers();
        this.isPrivate = isPrivate;
        gameState = State.QUEUED;
    }

    //<editor-fold defaultstate="collapsed" desc="Teams">
    private void createTeams(JSONTeamDeathMatch gamemode) {
        createRed(gamemode.getRedSpawn());
        createBlue(gamemode.getBlueSpawn());
    }

    private void createRed(JSONLocation spawn) {
        red.setPrefix("Red");
        red.setTeamColour(Color.RED);
        red.setKit(redKit());
        red.setSpawnLocations(new ArrayList<>(List.of(
                LocationTranscriber.TranscribeFromJSON(spawn)
        )));
        red.setGameMode(GameMode.ADVENTURE);
    }

    @Contract(value = " -> new", pure = true)
    private @NotNull Kit redKit() {
        Function<Player, Void> invFunc = (x -> {
            PlayerInventory returnInventory = x.getInventory();
            returnInventory.clear();
            returnInventory.setHelmet(new ItemStack(Material.LEATHER_HELMET));
            returnInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            returnInventory.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
            returnInventory.setBoots(new ItemStack(Material.LEATHER_BOOTS));
            returnInventory.setItemInOffHand(new ItemStack(Material.SHIELD));
            returnInventory.setItem(0, new ItemStack(Material.IRON_SWORD));
            ItemStack bow = new ItemStack(Material.BOW);
            bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
            returnInventory.setItem(1, bow);
            returnInventory.setItem(2, new ItemStack(Material.ARROW));
            returnInventory.forEach(item -> KitEditor.setItemColour(item, red.getTeamColour()));
            return null;
        });
        return new Kit(invFunc);
    }

    private void createBlue(JSONLocation spawn) {
        blue.setPrefix("Blue");
        blue.setTeamColour(Color.BLUE);
        blue.setKit(blueKit());
        blue.setSpawnLocations(new ArrayList<>(List.of(
                LocationTranscriber.TranscribeFromJSON(spawn)
        )));
        blue.setGameMode(GameMode.ADVENTURE);
    }

    @Contract(value = " -> new", pure = true)
    private @NotNull Kit blueKit() {
        Function<Player, Void> invFunc = (x -> {
            PlayerInventory returnInventory = x.getInventory();
            returnInventory.clear();
            returnInventory.setHelmet(new ItemStack(Material.LEATHER_HELMET));
            returnInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            returnInventory.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
            returnInventory.setBoots(new ItemStack(Material.LEATHER_BOOTS));
            returnInventory.setItemInOffHand(new ItemStack(Material.SHIELD));
            returnInventory.setItem(0, new ItemStack(Material.IRON_SWORD));
            ItemStack bow = new ItemStack(Material.BOW);
            bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
            returnInventory.setItem(1, bow);
            returnInventory.setItem(2, new ItemStack(Material.ARROW));
            returnInventory.forEach(item -> KitEditor.setItemColour(item, blue.getTeamColour()));
            return null;
        });
        return new Kit(invFunc);
    }

    private void createSpectator(JSONLocation spawn) {
        spectator.setPrefix("Spectator");
        spectator.setTeamColour(Color.SILVER);
        spectator.setSpawnLocations(new ArrayList<>(List.of(
                LocationTranscriber.TranscribeFromJSON(spawn)
        )));
        spectator.setGameMode(GameMode.SPECTATOR);
    }
    //</editor-fold>

    private void setRegion(List<JSONLocation> regionPoints, String mapName) {
        ArrayList<BlockVector2> wePoints = new ArrayList<>();
        World world = Bukkit.getWorld("world");
        try {
            for (JSONLocation e : regionPoints) {
                BlockVector2 point = BlockVector2.at(e.getX(), e.getZ());
                wePoints.add(point);

                region =new Polygonal2DRegion(new BukkitWorld(world), wePoints, 0, 1000);
            }
        } catch (Exception e) {
            throw new BadRegionException(mapName);
        }
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Start">
    @Override
    public void start() {
        PVPPlugin pvpPlugin = PVPPlugin.getInstance();
        //TODO: balance matchmaking
        AtomicBoolean joinRed = new AtomicBoolean(false);
        players.forEach(player -> {
            if(joinRed.get())
                Matchmaker.addMember(player, red);
            else
                Matchmaker.addMember(player, blue);
            joinRed.set(!joinRed.get());
        });
        PVPPlugin.getInstance().getServer().getOnlinePlayers().forEach(player ->{
            if(!blue.getActiveMembers().contains(player) && !red.getActiveMembers().contains(player))
                spectator.getActiveMembers().add(player);
        });
        createScoreboard();
        players.forEach(player -> player.setScoreboard(scoreboard));
        TeamHandler.spawnAll(red,blue,spectator);
        pvpPlugin.getPluginManager().registerEvents(new Listeners(), pvpPlugin);
        countDown();
    }
    private void countDown() {
        gameState = State.COUNTDOWN;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (countDownTimer == 0) {
                    players.forEach(player -> player.sendMessage(ChatColor.GREEN + "Game starts!"));
                    gameState = State.RUNNING;
                    this.cancel();
                    return;
                }
                players.forEach(player -> player.sendMessage(ChatColor.GREEN + "Game starts in " + countDownTimer));
                countDownTimer--;
            }
        }.runTaskTimer(PVPPlugin.getInstance(),0,20);
    }
    private void createScoreboard(){
        Objective Points = scoreboard.registerNewObjective("TDM Scoreboard", "dummy", "Remaining:");
        Points.getScore(ChatColor.BLUE + "Blue:").setScore(blue.getAlive());
        Points.getScore(ChatColor.DARK_RED + "Red:").setScore(red.getAlive());
        Points.setDisplaySlot(DisplaySlot.SIDEBAR);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="End">
    @Override
    public void end(boolean stopped) {
        PVPPlugin pvpPlugin = PVPPlugin.getInstance();
        if(!stopped){
            getWinningTeamMembers().forEach(player->{
                Playerstat playerstat = pvpPlugin.getPlayerstats().get(player.getUniqueId());
                playerstat.addWon();
                playerstat.addPlayed();
            });
            getLosingTeamMembers().forEach(player->{
                Playerstat playerstat = pvpPlugin.getPlayerstats().get(player.getUniqueId());
                playerstat.addLost();
                playerstat.addPlayed();
            });
        }
        players.forEach(player -> {
            player.getInventory().clear();
            player.getActivePotionEffects().clear();
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(pvpPlugin.getSpawn());
        });
        if (!stopped) spectator.getActiveMembers().forEach(player -> PVPPlugin.getInstance().getPlayerstats().get(player.getUniqueId()).addSpectate());
        gameState = State.ENDED;
        pvpPlugin.setActiveGame(null);
    }

    private Set<Player> getWinningTeamMembers() {
        if(red.getAlive() == 0)
            return blue.getActiveMembers();
        return red.getActiveMembers();
    }

    private Set<Player> getLosingTeamMembers() {
        if(red.getAlive() != 0)
            return blue.getActiveMembers();
        return red.getActiveMembers();
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Player join"
    @Override
    public List<String> tryJoin(Player player) {
        if(isPrivate && !whiteList.contains(player))
            return List.of(Style.ERROR + "You cannot join this game, you are not on the whitelist.");
        if(players.contains(player))
            return List.of(Style.ERROR + "You are already part of this game.");
        if(gameState == State.COUNTDOWN)
            return List.of(Style.ERROR + "You cannot join the game during countdown, please wait before retrying.");
        if(maxPlayers <=players.size())
            return List.of(Style.ERROR + "Cannot join game, the game is already full.");
        players.add(player);
        return joinGame(player);
    }
    private List<String> joinGame(Player player) {
        if(red.getDeadMembers().contains(player))
            return joinDeadRed(player);
        if(blue.getDeadMembers().contains(player))
            return joinDeadBlue(player);
        return(joinRandom(player));
    }
    private List<String> joinDeadRed(Player player){
        red.getActiveMembers().add(player);
        TeamHandler.spawn(player, spectator);
        player.setGameMode(GameMode.SPECTATOR);
        return List.of(Color.RED + player.getName() + " has joined, but was already dead.");
    }
    private List<String> joinRed(Player player) {
        Matchmaker.addMember(player,red);
        TeamHandler.spawn(player,red);
        player.setGameMode(GameMode.ADVENTURE);
        updateScoreboard();
        return List.of(Color.RED + player.getName() + "  has joined the red team!");
    }
    private List<String> joinDeadBlue(Player player){
        blue.getActiveMembers().add(player);
        TeamHandler.spawn(player, spectator);
        player.setGameMode(GameMode.SPECTATOR);
        return List.of(Color.BLUE + player.getName() + " has joined, but was already dead.");
    }
    private List<String> joinBlue(Player player) {
        Matchmaker.addMember(player, blue);
        TeamHandler.spawn(player,blue);
        player.setGameMode(GameMode.ADVENTURE);
        updateScoreboard();
        return List.of(Color.BLUE + player.getName() + "  has joined the blue team!");
    }
    private List<String> joinRandom(Player player){
        //todo: ELO shit
        if(blue.getAlive() <=red.getAlive())
            return joinBlue(player);
        return joinRed(player);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Player leave"
    @Override
    public void leaveGame(Player player) {
        red.getActiveMembers().remove(player);
        blue.getActiveMembers().remove(player);
        player.getInventory().clear();
        spectator.getActiveMembers().add(player);
        checkEnd();
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Checks"
    @Override
    public boolean canStart() {
        return !(red.getActiveMembers().isEmpty() ||
                blue.getActiveMembers().isEmpty());
    }
    private void checkEnd() {
        if(red.getAlive() == 0 || blue.getAlive() == 0)
            end(false);
    }
    //</editor-fold>
    private void updateScoreboard(){
        Objective Points = scoreboard.getObjective("TDM Scoreboard");
        if (Points != null) {
            Points.getScore(ChatColor.BLUE + "Blue:").setScore(blue.getAlive());
            Points.getScore(ChatColor.DARK_RED + "Red:").setScore(red.getAlive());
        }
        else
            Logger.getLogger("PVPPlugin").log(Level.SEVERE, "Scoreboard has failed for Team Deathmatch");
    }
    private class Listeners implements GamemodeListener {

        HashMap<UUID, Long> playerAreaLeaveTimer = new HashMap<>();
        @EventHandler
        public void onPlayerDeath(PlayerDeathEvent e){
            Player player = e.getEntity();
            if(!players.contains(player))
                return;
            if(blue.getActiveMembers().contains(player)){
                blue.getDeadMembers().add(player);
            }
            if(red.getActiveMembers().contains(player)){
                red.getDeadMembers().add(player);
            }
            player.setGameMode(GameMode.SPECTATOR);
            PVPPlugin.getInstance().getPlayerstats().get(player.getUniqueId()).addDeath();
            updateScoreboard();
            checkEnd();
        }
        @EventHandler
        public void onPlayerLeave(PlayerQuitEvent e){
            Player player = e.getPlayer();
            red.getActiveMembers().remove(player);
            blue.getActiveMembers().remove(player);
            player.getInventory().clear();
            players.remove(player);
            updateScoreboard();
            checkEnd();
        }
        @EventHandler
        public void onPlayerMove(PlayerMoveEvent e) {
            Location from = e.getFrom();
            Location to = e.getTo();

            Player player = e.getPlayer();
            UUID uuid = player.getUniqueId();


            if(gameState == State.QUEUED)
                return;
            if(gameState == State.COUNTDOWN &&
                    !spectator.getActiveMembers().contains(e.getPlayer()) &&
                    (from.getX() != to.getX() || from.getZ() != to.getZ())) {
                e.setCancelled(true);
                return;
            }

            if(!region.contains(BlockVector3.at(to.getX(), to.getY(), to.getZ()))){
                e.setCancelled(true);
                if(!playerAreaLeaveTimer.containsKey(uuid)){
                    player.sendMessage(ChatColor.RED + "You aren't allowed to leave the map!");
                    playerAreaLeaveTimer.put(uuid, System.currentTimeMillis());
                    return;
                }
                if(System.currentTimeMillis() - playerAreaLeaveTimer.get(uuid) > 3000){
                    player.sendMessage(ChatColor.RED + "You aren't allowed to leave the map!");
                    playerAreaLeaveTimer.put(uuid, System.currentTimeMillis());
                }
            }
        }
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent e){
            Player player = e.getPlayer();
            if(PVPPlugin.getInstance().isPVPServer()) {
                Matchmaker.addMember(player, spectator);
                TeamHandler.spawn(player, spectator);
            }
        }

    }
    private class TDMTeam extends Team{
        Set<Player> deadMembers = new HashSet<>();

        public Set<Player> getDeadMembers(){
            return deadMembers;
        }

        public Integer getAlive(){
            Set<Player> members = activeMembers;
            members.removeAll(deadMembers);
            return members.size();
        }
    }
}