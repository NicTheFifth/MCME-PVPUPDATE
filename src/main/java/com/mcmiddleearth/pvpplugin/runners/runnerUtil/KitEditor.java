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

    public static void setUnbreaking(ItemStack item){
        if(item == null)
            return;
        ItemMeta meta = item.hasItemMeta() ?
                item.getItemMeta() :
                Bukkit.getItemFactory().getItemMeta(item.getType());
        if(meta == null)
            return;
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
    }
    public static void setItemColour(ItemStack item, Color teamColour) {
        if(item == null)
            return;
        if(Set.of(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS).contains(item.getType())){
            ItemMeta meta = item.hasItemMeta() ?
                item.getItemMeta() :
                Bukkit.getItemFactory().getItemMeta(item.getType());
            if(meta instanceof LeatherArmorMeta leatherArmorMeta) {
                leatherArmorMeta.setColor(teamColour);
                item.setItemMeta(leatherArmorMeta);
            }
        }
        if(item.getType().equals(Material.SHIELD)){
            ItemMeta meta = item.hasItemMeta() ?
                    item.getItemMeta() :
                    Bukkit.getItemFactory().getItemMeta(item.getType());
            DyeColor dyeColor = DyeColor.getByColor(teamColour);
            if(dyeColor == null)
                return;
            if(meta instanceof BlockStateMeta blockStateMeta) {
                Banner banner = (Banner) blockStateMeta.getBlockState();
                banner.setBaseColor(dyeColor);
                blockStateMeta.setBlockState(banner);
                item.setItemMeta(blockStateMeta);
            }
        }
    }
}
