package com.mcmiddleearth.mcme.pvpplugin.gamemodes.helpers;


import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Kits_Classes {
    @Getter
    PlayerInventory inventory;

    public void Kit(Material offHand, Material helmet, Material chestplate, Material trousers, Material boots, Material weapon, Material rangedWeapon) {
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
