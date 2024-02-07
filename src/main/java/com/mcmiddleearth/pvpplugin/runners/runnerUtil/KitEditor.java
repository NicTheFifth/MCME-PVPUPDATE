package com.mcmiddleearth.pvpplugin.runners.runnerUtil;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Set;

public class KitEditor {
    public static void setItemColour(ItemStack item, Color teamColour) {
        if(item == null)
            return;
        if(Set.of(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS).contains(item.getType())){
            ItemMeta meta = item.hasItemMeta() ?
                item.getItemMeta() :
                Bukkit.getItemFactory().getItemMeta(item.getType());
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
            leatherArmorMeta.setColor(teamColour);
            item.setItemMeta(leatherArmorMeta);
        }
        if(item.getType().equals(Material.SHIELD)){
            //TODO Create shield colouring
        }
    }
}
