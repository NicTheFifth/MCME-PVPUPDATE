package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.exceptions.BadRegionException;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.Playerstat;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamDeathMatch;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.KitEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.pvpplugin.util.Kit;
import com.mcmiddleearth.pvpplugin.util.Matchmaker;
import com.mcmiddleearth.pvpplugin.util.Team;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
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

    private enum State{
        QUEUED, COUNTDOWN, RUNNING, ENDED
    }

    int maxPlayers;
    State gameState;

    private long countDownTimer = 5;

    Boolean isPrivate;

    Set<Player> players = new HashSet<>();

    Team red = new Team();

    Team blue = new Team();

    Team spectator = new Team();

    Region region;

    Scoreboard scoreboard;

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

    @Override
    public boolean canStart() {
        return !(red.getActiveMembers().isEmpty() || blue.getActiveMembers().isEmpty());
    }

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
        createScoreboard();
        players.forEach(player -> player.setScoreboard(scoreboard));
        TeamHandler.spawnAll(red,blue,spectator);
        TeamHandler.setGamemode(GameMode.ADVENTURE,red,blue);
        TeamHandler.setGamemode(GameMode.SPECTATOR, spectator);
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
        Points.getScore(ChatColor.BLUE + "Blue:").setScore(blue.getAliveMembers().size());
        Points.getScore(ChatColor.DARK_RED + "Red:").setScore(red.getAliveMembers().size());
        Points.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    private void updateScoreboard(){
        Objective Points = scoreboard.getObjective("TDM Scoreboard");
        if (Points != null) {
            Points.getScore(ChatColor.BLUE + "Blue:").setScore(blue.getAliveMembers().size());
            Points.getScore(ChatColor.DARK_RED + "Red:").setScore(red.getAliveMembers().size());
        }
        else
            Logger.getLogger("PVPPlugin").log(Level.SEVERE, "Scoreboard has failed for Team Deathmatch");
    }
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
            //TODO: teleport all players back to spawn
            player.teleport(pvpPlugin.getServer().getWorld(player.getWorld().getName()).getSpawnLocation());
        });
        if (!stopped) spectator.getActiveMembers().forEach(player -> PVPPlugin.getInstance().getPlayerstats().get(player.getUniqueId()).addSpectate());
        gameState = State.ENDED;
        pvpPlugin.setActiveGame(null);
    }

    private Set<Player> getWinningTeamMembers() {
        if(red.getDeadMembers().containsAll(red.getActiveMembers()))
            return blue.getActiveMembers();
        return red.getActiveMembers();
    }

    private Set<Player> getLosingTeamMembers() {
        if(!red.getDeadMembers().containsAll(red.getActiveMembers()))
            return blue.getActiveMembers();
        return red.getActiveMembers();
    }

    @Override
    public List<String> tryJoin(Player player) {
        if(players.contains(player))
            return List.of(Style.ERROR + "You are already part of this game.");
        if(gameState == State.COUNTDOWN)
            return List.of(Style.ERROR + "You cannot join the game during countdown, please wait before retrying.");
        if(maxPlayers <=players.size())
            return List.of(Style.ERROR + "Cannot join game, the game is already full.");
        players.add(player);
        return joinGame(player);
    }

    @Override
    public List<String> joinGame(Player player) {
        //TODO: implement joinGame correctly
        if(blue.getActiveMembers().size() <=red.getActiveMembers().size()){
            Matchmaker.addMember(player, blue);
            TeamHandler.spawn(player,blue);
            player.setGameMode(GameMode.ADVENTURE);
            updateScoreboard();
            return List.of(Color.BLUE + player.getName() + "  has joined the blue team!");
        }
        Matchmaker.addMember(player,red);
        TeamHandler.spawn(player,red);
        player.setGameMode(GameMode.ADVENTURE);
        updateScoreboard();
        return List.of(Color.RED + player.getName() + "  has joined the red team!");
    }

    @Override
    public void leaveGame(Player player) {
        red.getActiveMembers().remove(player);
        blue.getActiveMembers().remove(player);
        player.getInventory().clear();
        spectator.getActiveMembers().add(player);
        checkEnd();
    }

    @Override
    public void leaveServer(Player player){
        red.getActiveMembers().remove(player);
        blue.getActiveMembers().remove(player);
        player.getInventory().clear();
        players.remove(player);
        checkEnd();

    }


    private void checkEnd() {
    }
//
//    Team spectator = new Team();
//
//    Set<Player> players = new HashSet<>();
//
//    public TeamDeathMatchRunner(JSONMap map){
//        JSONTeamDeathMatch tdm = map.getJSONTeamDeathMatch();
//        JSONLocation redSpawn = tdm.getRedSpawn();
//        JSONLocation blueSpawn = tdm.getBlueSpawn();
//        JSONLocation spectatorSpawn = map.getSpawn();
//        red.setSpawnLocations(new ArrayList<>(List.of(
//                LocationTranscriber.TranscribeFromJSON(redSpawn)
//        )));
//        blue.setSpawnLocations(new ArrayList<>(List.of(
//                LocationTranscriber.TranscribeFromJSON(blueSpawn)
//        )));
//        spectator.setSpawnLocations(new ArrayList<>(List.of(
//                LocationTranscriber.TranscribeFromJSON(spectatorSpawn)
//        )));
//    }
//    void join(Player player){
//        players.add(player);
//        Integer eloRed = getElo(red);
//        Integer eloBlue = getElo(blue);
//        if(eloRed <= eloBlue){
//            addToTeam(red,player);
//            broadcastJoin(player,"red");
//            return;
//        }
//        addToTeam(blue,player);
//        broadcastJoin(player,"blue");
//    }
//
//    private Integer getElo(Team team) {
//        return 0;
//    }
//
//    private void broadcastJoin(Player player, String blue) {
//    }
//
//    private void addToTeam(Team red, Player player) {
//    }
//
//    void leave(Player player){
//        leaveTeam(red,player);
//        leaveTeam(blue,player);
//        if(red.getMembers().isEmpty() || blue.getMembers().isEmpty()){
//            end(false);
//        }
//        broadcastLeave(player);
//    }
//
//    private void broadcastLeave(Player player) {
//    }
//
//    private void leaveTeam(Team red, Player player) {
//    }
//
//    void end(boolean ended){
//
//    }
    private class Listeners implements Listener{

        @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){

        }
}
}
