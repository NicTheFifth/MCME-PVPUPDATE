package com.mcmiddleearth.pvpplugin.runners.listeners;

import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class ArrowRestocker extends GamemodeListener {
    static final int MAX_ARROWS = 30;
    static final int ARROWS_PER_RESTOCK = 10;
    static final int SECONDS_COOLDOWN = 1;

    final HashMap<UUID, Long> playerRestockTimer = new HashMap<>();

    public ArrowRestocker(@NotNull GamemodeRunner runner) {
        super(runner);
    }

    @EventHandler
    public void onChestOpen(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(runner.getGameState() != GamemodeRunner.State.RUNNING)
            return;
        if(!runner.getPlayers().contains(player))
            return;
        if(e.getClickedBlock() == null)
            return;
        if(!e.getClickedBlock().getType().equals(Material.CHEST))
            return;
        if(!player.getInventory().contains(Material.ARROW, MAX_ARROWS))
            return;
        e.setCancelled(true);
        UUID uuid = player.getUniqueId();
        if(!playerRestockTimer.containsKey(uuid)){
            stockArrows(player);
        }
        if(System.currentTimeMillis() - playerRestockTimer.get(uuid) < SECONDS_COOLDOWN * 1000){
            return;
        }
        stockArrows(player);
    }

    void stockArrows(Player player){
        playerRestockTimer.put(player.getUniqueId(), System.currentTimeMillis());
        ItemStack is = new ItemStack(Material.ARROW, ARROWS_PER_RESTOCK);
        player.getInventory().addItem(is);
    }

    public void unregister(){
        PlayerInteractEvent.getHandlerList().unregister(this);
    }
}
