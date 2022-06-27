package com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.Playerstat;
import com.mcmiddleearth.mcme.pvpplugin.json.transcribers.TeamSlayerTranscriber;
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
    public void start() {
        ScoreboardEditor.InitTeamSlayer(scoreboard, red, blue, pointLimit);
        PVPPlugin.getInstance().getPluginManager().registerEvents(this, PVPPlugin.getInstance());
        Matchmaker.TeamSlayerMatchMake(players, red, blue);
        TeamHandler.spawnAll(red, blue, spectator);
        TeamHandler.setGamemode(GameMode.SURVIVAL, red, blue);
        super.start();
        run();
    }

    public void run() {
    }

    @Override
    public boolean canStart() {
        return pointLimit == null || super.canStart();
    }

    @Override
    public void end(boolean stopped) {
        if (!stopped) {
            GetWinningTeam().getMembers().forEach(player -> {
                Playerstat playerstat = PVPPlugin.getInstance().getPlayerstats().get(player.getUniqueId());
                playerstat.addWon();
                playerstat.addPlayed();
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

    @Override
    protected boolean canJoin(Player player) {
        return super.canJoin(player);
    }

    @Override
    public String[] join(Player player) {
        super.join(player);
        if (gameState != State.QUEUED) {
            PVPPlugin.getInstance().getMatchmaker().addMember(player, red, blue);
        }
        //TODO: handle update
        return null;
    }

    @Override
    public void leave(Player player) {
        if (blue.getMembers().contains(player)) {
            blue.getMembers().remove(player);
            blue.getDeadMembers().add(player);
        }
        red.getMembers().remove(player);
        super.leave(player);
    }

    private void InitialiseRed() {
        red.setPrefix("Red");
        red.setTeamColour(Color.RED);
        red.setKit(RedKit());
    }

    private Kit RedKit() {
        PlayerInventory returnInventory = (PlayerInventory) Bukkit.createInventory(null, InventoryType.PLAYER);
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
        return new Kit(returnInventory);
    }

    private void InitialiseBlue() {
        blue.setPrefix("Blue");
        blue.setTeamColour(Color.BLUE);
        blue.setKit(BlueKit());
    }

    private Kit BlueKit() {
        PlayerInventory returnInventory = (PlayerInventory) Bukkit.createInventory(null, InventoryType.PLAYER);
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
        return new Kit(returnInventory);
    }

//<editor-fold defaultstate="collapsed" desc="delombok">
//</editor-fold>
    //TODO: Fix everything from here.
    private Team GetWinningTeam() {
        if (blue.getMembers().isEmpty()) {
            return red;
        }
        return blue;
    }

//<editor-fold defaultstate="collapsed" desc="delombok">
//</editor-fold>
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
                red.getMembers().add(player);
            }
            ScoreboardEditor.updateValueTeamSlayer(scoreboard, red, blue);
            handleDeath(playerDeath);
        }
    }

    @EventHandler
    public void onPlayerSpawn(PlayerRespawnEvent playerRespawn) {
        Player player = playerRespawn.getPlayer();
        if (players.contains(player)) {
            if (red.getMembers().contains(player)) {
                playerRespawn.setRespawnLocation(red.getSpawnLocations().get(0));
            }
            if (blue.getMembers().contains(player)) {
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
