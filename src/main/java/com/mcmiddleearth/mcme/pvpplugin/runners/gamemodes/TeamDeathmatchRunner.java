package com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.Playerstat;
import com.mcmiddleearth.mcme.pvpplugin.json.transcribers.TeamDeathMatchTranscriber;
import com.mcmiddleearth.mcme.pvpplugin.runners.runnerUtil.KitEditor;
import com.mcmiddleearth.mcme.pvpplugin.runners.runnerUtil.ScoreboardEditor;
import com.mcmiddleearth.mcme.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.mcme.pvpplugin.util.Kit;
import com.mcmiddleearth.mcme.pvpplugin.util.Matchmaker;
import com.mcmiddleearth.mcme.pvpplugin.util.Team;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.function.Function;

public class TeamDeathmatchRunner extends BaseRunner {
    Team red = new Team();
    Team blue = new Team();

    public TeamDeathmatchRunner(JSONMap map, boolean privateGame) {
        this.privateGame = privateGame;
        TeamDeathMatchTranscriber.Transcribe(map, this);
        InitialiseRed();
        InitialiseBlue();
        gameState = State.QUEUED;
    }

    @Override
    public boolean canStart() {
        return !getPlayers().isEmpty() && super.canStart();
    }

    @Override
    public void start() {
        ScoreboardEditor.InitTeamDeathmatch(scoreboard, red, blue);
        PVPPlugin.getInstance().getPluginManager().registerEvents(this, PVPPlugin.getInstance());
        Matchmaker.teamDeathmatchMatchMake(players, red, blue);
        TeamHandler.spawnAll(red, blue);
        TeamHandler.setGamemode(GameMode.SURVIVAL, red, blue);
        super.start();
    }

    @Override
    public void end(boolean stopped) {
        if (!stopped) {
            GetWinningTeam().getMembers().forEach(player -> {
                Playerstat playerstat = PVPPlugin.getInstance().getPlayerstats().get(player.getUniqueId());
                playerstat.addWon();
                playerstat.addPlayed();
                player.sendMessage((GetWinningTeam()) + " Team won the Game!");
            });
            GetLosingTeam().getMembers().forEach(player -> {
                Playerstat playerstat = PVPPlugin.getInstance().getPlayerstats().get(player.getUniqueId());
                playerstat.addLost();
                playerstat.addPlayed();
            });
        }
        HandlerList.unregisterAll(this);
        super.end(stopped);
    }

    //FIXME: Fix join shit
    @Override
    public List<String> tryJoin(Player player) {
        super.tryJoin(player);
        if (gameState != State.QUEUED) {
            PVPPlugin.getInstance().getMatchmaker().addMember(player, red, blue);
            ScoreboardEditor.updateValueTeamDeathmatch(scoreboard, red, blue);
        }
        return null;
    }

    @Override
    public void leave(Player player, boolean failedJoin) {
        if (blue.getMembers().contains(player)) {
            blue.getMembers().remove(player);
            blue.getDeadMembers().add(player);
        }
        red.getMembers().remove(player);
        ScoreboardEditor.updateValueTeamDeathmatch(scoreboard, red, blue);
        super.leave(player, false);
    }

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

    private Team GetWinningTeam() {
        if (blue.getMembers().isEmpty()) {
            return red;
        }
        return blue;
    }

    private Team GetLosingTeam() {
        if (blue.getMembers().isEmpty()) {
            return blue;
        }
        return red;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent playerDeath) {
        Player player = playerDeath.getEntity();
        if (players.contains(player)) {
            if (blue.getMembers().remove(player)) {
                blue.getDeadMembers().add(player);
                red.getMembers().remove(player);
                red.getDeadMembers().add(player);
            }
            spectator.getMembers().add(player);
            ScoreboardEditor.updateValueTeamDeathmatch(scoreboard, red, blue);
            handleDeath(playerDeath);
            CheckWinCondition();
        }
    }

    @EventHandler
    public void onPlayerSpawn(PlayerRespawnEvent playerRespawn) {
        Player player = playerRespawn.getPlayer();
        if (spectator.getMembers().contains(player)) {
            playerRespawn.setRespawnLocation(spectator.getSpawnLocations().get(0));
        }
        if (red.getMembers().contains(player)) {
            playerRespawn.setRespawnLocation(red.getSpawnLocations().get(0));
        }
        if (red.getMembers().contains(player)) {
            playerRespawn.setRespawnLocation(red.getSpawnLocations().get(0));
        }
    }

    public void CheckWinCondition() {
        if (red.getMembers().isEmpty()) {
            this.end(false);
        }
        if (blue.getMembers().isEmpty()) {
            this.end(false);
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
    //</editor-fold>
}
