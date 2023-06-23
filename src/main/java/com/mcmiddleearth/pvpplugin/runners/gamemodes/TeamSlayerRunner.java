package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.Playerstat;
import com.mcmiddleearth.pvpplugin.json.transcribers.TeamSlayerTranscriber;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.KitEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.ScoreboardEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.pvpplugin.util.Kit;
import com.mcmiddleearth.pvpplugin.util.Matchmaker;
import com.mcmiddleearth.pvpplugin.util.Team;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.function.Function;

public class TeamSlayerRunner extends BaseRunner {
    Team red = new Team();
    Team blue = new Team();
    Integer pointLimit;

    public TeamSlayerRunner(JSONMap map, boolean privateGame) {
        this.privateGame = privateGame;
        TeamSlayerTranscriber.Transcribe(map, this);
        InitialiseRed();
        InitialiseBlue();
        gameState = State.QUEUED;
    }

    @Override
    public boolean canStart() {
        return pointLimit != null || super.canStart();
    }

    @Override
    public void start() {
        ScoreboardEditor.InitTeamSlayer(scoreboard, red, blue, pointLimit);
        PVPPlugin.getInstance().getPluginManager().registerEvents(this, PVPPlugin.getInstance());
        Matchmaker.TeamSlayerMatchMake(players, red, blue);
        TeamHandler.spawnAll(red, blue, spectator);
        TeamHandler.setGamemode(GameMode.SURVIVAL, red, blue);
        super.start();
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
        HandlerList.unregisterAll(this);
        super.end(stopped);
    }


    @Override
    public List<String> tryJoin(Player player) {
        super.tryJoin(player);
        if (gameState != State.QUEUED) {
            PVPPlugin.getInstance().getMatchmaker().addMember(player, red, blue);
        }
        //TODO: handle update
        return null;
    }

    @Override
    public void leave(Player player, boolean failedJoin) {
        if (blue.getActiveMembers().contains(player)) {
            blue.getActiveMembers().remove(player);
            blue.getDeadMembers().add(player);
        }
        red.getActiveMembers().remove(player);
        super.leave(player, false);
    }

    //<editor-fold desc="Initialisation of teams">
    private void InitialiseRed() {
        red.setPrefix("Red");
        red.setTeamColour(Color.RED);
        red.setKit(RedKit());
    }

    private Kit RedKit() {
        Function<Player, Void> invFunc = (x -> {
            PlayerInventory returnInventory = x.getInventory();
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
            returnInventory.forEach(item -> KitEditor.setItemColour(item, red.getTeamColour()));
            return null;
        });
        return new Kit(invFunc);
    }

    private void InitialiseBlue() {
        blue.setPrefix("Blue");
        blue.setTeamColour(Color.BLUE);
        blue.setKit(BlueKit());
    }

    private Kit BlueKit() {
        Function<Player, Void> invFunc = (x -> {
            PlayerInventory returnInventory = x.getInventory();
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
            returnInventory.forEach(item -> KitEditor.setItemColour(item, blue.getTeamColour()));
            return null;
        });
        return new Kit(invFunc);
    }
    //</editor-fold>

    //TODO: Fix everything from here.
    private Team GetWinningTeam() {
        if (blue.getActiveMembers().isEmpty()) {
            return red;
        }
        return blue;
    }

//<editor-fold defaultstate="collapsed" desc="delombok">
//</editor-fold>
    private Team GetLosingTeam() {
        if (blue.getActiveMembers().isEmpty()) {
            return blue;
        }
        return red;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent playerDeath) {
        Player player = playerDeath.getEntity();
        if (players.contains(player)) {
            if (blue.getActiveMembers().remove(player)) {
                blue.getDeadMembers().add(player);
                red.getActiveMembers().add(player);
            }
            ScoreboardEditor.updateValueTeamSlayer(scoreboard, red, blue);
            handleDeath(playerDeath);
        }
    }

    @EventHandler
    public void onPlayerSpawn(PlayerRespawnEvent playerRespawn) {
        Player player = playerRespawn.getPlayer();
        if (players.contains(player)) {
            if (red.getActiveMembers().contains(player)) {
                playerRespawn.setRespawnLocation(red.getSpawnLocations().get(0));
            }
            if (blue.getActiveMembers().contains(player)) {
                playerRespawn.setRespawnLocation(red.getSpawnLocations().get(0));
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public Team getRed() {
        return this.red;
    }

    @SuppressWarnings("all")
    public Team getBlue() {
        return this.blue;
    }

    @SuppressWarnings("all")
    public Integer getPointLimit() {
        return this.pointLimit;
    }

    @SuppressWarnings("all")
    public void setPointLimit(final Integer pointLimit) {
        this.pointLimit = pointLimit;
    }
    //</editor-fold>
}
