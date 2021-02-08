package com.mcmiddleearth.mcme.pvpplugin.Gamemodes.gamemode_helpers;


import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Kits_Classes {
    public enum Kit {
        Basic(Material.SHIELD, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.IRON_SWORD, Material.BOW),
        RingBearer(null, Material.GLOWSTONE, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.IRON_SWORD, Material.BOW),
        Infected(Material.SHIELD, null, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, null, Material.IRON_SWORD, Material.BOW),
        OneInTheQuiver(null, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.IRON_AXE, Material.CROSSBOW);
        @Getter
        private final PlayerInventory inventory;

        Kit(Material offHand, Material helmet, Material chestplate, Material trousers, Material boots, Material weapon, Material rangedWeapon) {
            this.inventory = (PlayerInventory) Bukkit.createInventory(null, InventoryType.PLAYER);
            if(offHand != null) {
                this.inventory.setItemInOffHand(new ItemStack(offHand));
            }
            if (weapon != null) {
                this.inventory.setItem(0, new ItemStack(weapon));
            }
            if(rangedWeapon != null) {
                this.inventory.setItem(1, new ItemStack(rangedWeapon));
            }
            if(helmet != null){
                this.inventory.setHelmet(new ItemStack(helmet));
            }
            if(chestplate != null){
                this.inventory.setChestplate(new ItemStack(chestplate));
            }
            if(trousers != null){
                this.inventory.setLeggings(new ItemStack(trousers));
            }
            if(boots != null){
                this.inventory.setBoots(new ItemStack(boots));
            }
        }
    }

    public void setKit(Player player, Kit kit, Color color){

    }
}
