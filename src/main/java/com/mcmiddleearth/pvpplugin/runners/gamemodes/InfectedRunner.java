package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.Playerstat;
import com.mcmiddleearth.pvpplugin.json.transcribers.InfectedTranscriber;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.ScoreboardEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.pvpplugin.util.Kit;
import com.mcmiddleearth.pvpplugin.util.Team;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.KitEditor;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.List;
import java.util.function.Function;

import static com.mcmiddleearth.pvpplugin.util.Matchmaker.*;

public class InfectedRunner extends BaseRunner {
    Team infected = new Team();
    Team survivors = new Team();
    Integer timeSec;

    public InfectedRunner(JSONMap map, boolean privateGame) {
        this.privateGame = privateGame;
        InfectedTranscriber.Transcribe(map, this);
        InitialiseInfected();
        InitialiseSurvivors();
        gameState = State.QUEUED;
    }

    @Override
    public boolean canStart() {
        return timeSec != null && super.canStart();
    }

    @Override
    public void start() {
        PVPPlugin.getInstance().getPluginManager().registerEvents(this, PVPPlugin.getInstance());
        PVPPlugin.getInstance().getMatchmaker().infectedMatchMake(players, infected, survivors);
        ScoreboardEditor.InitInfected(scoreboard, infected, survivors, timeSec);
        players.forEach(p -> p.setScoreboard(scoreboard));
        TeamHandler.spawnAll(infected, survivors, spectator);
        TeamHandler.setGamemode(GameMode.ADVENTURE, infected, survivors);
        super.start();
        run();
    }

    private void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(gameState == State.RUNNING) {
                    timeSec--;
                    if (timeSec == 0 || gameState == State.ENDED) {
                        end(false);
                        this.cancel();
                        return;
                    }
                    if (timeSec == 1) {
                        players.forEach(player -> player.sendMessage(ChatColor.GREEN + "1 second remaining!"));
                    }
                    if (timeSec >= 2 && timeSec <= 5) {
                        players.forEach(player -> player.sendMessage(ChatColor.GREEN + String.format("%d seconds remaining!", timeSec)));
                    }
                    if (timeSec == 30) {
                        players.forEach(player -> player.sendMessage(ChatColor.GREEN + "30 seconds remaining!"));
                    }
                    ScoreboardEditor.updateTime(scoreboard, timeSec);
                }
            }
        }.runTaskTimer(PVPPlugin.getInstance(), 0, 20L );
    }

    @Override
    public void end(boolean stopped) {
        if (!stopped) {
            GetWinningTeam().getActiveMembers().forEach(player -> {
                Playerstat playerstat = PVPPlugin.getInstance().getPlayerstats().get(player.getUniqueId());
                playerstat.addWon();
                playerstat.addPlayed();
            });
            GetLosingTeam().getActiveMembers().forEach(player -> {
                Playerstat playerstat = PVPPlugin.getInstance().getPlayerstats().get(player.getUniqueId());
                playerstat.addLost();
                playerstat.addPlayed();
            });
        }
        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        HandlerList.unregisterAll(this);
        super.end(stopped);
    }

    @Override
    public List<String> tryJoin(Player player) {
        List<String> playerMessage = super.tryJoin(player);
        if(playerMessage.isEmpty()) {
            if (gameState != State.QUEUED) {
                if (survivors.getDeadMembers().contains(player)) {
                    addMember(player, infected);
                    playerMessage.add(ChatColor.RED + "Joined the infected team!");
                }
                else {
                    addMember(player, survivors);
                    playerMessage.add(ChatColor.BLUE + "Joined the survivor team!");
                }
                ScoreboardEditor.updateValueInfected(scoreboard, infected, survivors);
            }
            playerMessage.add(Style.INFO + "Joined the game!");
        }
        return playerMessage;
    }

    @Override
    public void leave(Player player, boolean failedJoin) {
        if (survivors.getActiveMembers().contains(player)) {
            survivors.getActiveMembers().remove(player);
            survivors.getDeadMembers().add(player);
        }
        infected.getActiveMembers().remove(player);
        ScoreboardEditor.updateValueInfected(scoreboard, infected, survivors);
        super.leave(player, false);
    }

    //<editor-fold desc="Initialising teams">
    private void InitialiseInfected() {
        infected.setPrefix("Infected");
        infected.setTeamColour(Color.RED);
        infected.setKit(InfectedKit());
    }

    private Kit InfectedKit() {
        Function<Player, Void> invFunc = (x -> {
            PlayerInventory returnInventory = x.getInventory();
            returnInventory.clear();
            returnInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            returnInventory.setItemInOffHand(new ItemStack(Material.SHIELD));
            returnInventory.setItem(0, new ItemStack(Material.IRON_SWORD));
            ItemStack bow = new ItemStack(Material.BOW);
            bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
            returnInventory.setItem(1, bow);
            returnInventory.setItem(2, new ItemStack(Material.ARROW));
            returnInventory.forEach(item -> KitEditor.setItemColour(item, infected.getTeamColour()));
            return null;
        });
        return new Kit(invFunc);
    }

    private void InitialiseSurvivors() {
        survivors.setPrefix("Survivor");
        survivors.setTeamColour(Color.BLUE);
        survivors.setKit(SurvivorKit());
    }

    private Kit SurvivorKit() {
        Function<Player, Void> invFunc = (x -> {
            PlayerInventory returnInventory = x.getInventory();
            returnInventory.clear();
            returnInventory.setBoots(new ItemStack(Material.LEATHER_BOOTS));
            returnInventory.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
            returnInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            returnInventory.setHelmet(new ItemStack(Material.LEATHER_HELMET));
            returnInventory.setItemInOffHand(new ItemStack(Material.SHIELD));
            returnInventory.setItem(0, new ItemStack(Material.IRON_SWORD));
            ItemStack bow = new ItemStack(Material.BOW);
            bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
            returnInventory.setItem(1, bow);
            returnInventory.setItem(2, new ItemStack(Material.ARROW));
            returnInventory.forEach(item -> KitEditor.setItemColour(item, survivors.getTeamColour()));
            return null;
        });
        return new Kit(invFunc);
    }
    //</editor-fold>

    //<editor-fold desc="EventHandlers">
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent playerLeave) {
        Player player = playerLeave.getPlayer();
        leave(player, true);
        if (infected.getActiveMembers().isEmpty()) {
            this.end(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent playerDeath) {
        Player player = playerDeath.getEntity();
        if (players.contains(player)) {
            if (survivors.getActiveMembers().remove(player)) {
                survivors.getDeadMembers().add(player);
                infected.getActiveMembers().add(player);
            }
            ScoreboardEditor.updateValueInfected(scoreboard, infected, survivors);
            handleDeath(playerDeath);
        }
        if (survivors.getActiveMembers().isEmpty()) {
            this.end(false);
        }
    }

    @EventHandler
    public void onPlayerSpawn(PlayerRespawnEvent playerRespawn) {
        Player player = playerRespawn.getPlayer();
        if (players.contains(player)) {
            if (infected.getActiveMembers().contains(player)) {
                playerRespawn.setRespawnLocation(infected.getSpawnLocations().get(0));
            }
            if (survivors.getActiveMembers().contains(player)) {
                playerRespawn.setRespawnLocation(infected.getSpawnLocations().get(0));
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Team getInfected() {
        return this.infected;
    }
    public Team getSurvivors() {
        return this.survivors;
    }

    public Integer getTimeSec() {
        return this.timeSec;
    }
    public void setTimeMin(final Integer timeMin) {
        this.timeSec = timeMin*60;
    }

    private Team GetWinningTeam() {
        if (survivors.getActiveMembers().isEmpty()) {
            return infected;
        }
        return survivors;
    }
    private Team GetLosingTeam() {
        if (survivors.getActiveMembers().isEmpty()) {
            return survivors;
        }
        return infected;
    }
    //</editor-fold>
}
