package com.mcmiddleearth.pvpplugin.runners.runnerUtil;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class KitEditor {
    public static void setItemColour(ItemStack item, Color teamColour) {
        if(item == null)
            return;
        if(item instanceof LeatherArmorMeta){
            LeatherArmorMeta newColour = (LeatherArmorMeta) item;
            newColour.setColor(teamColour);
            item.setItemMeta(newColour);
        }
        if(item.getType().equals(Material.SHIELD)){
            //TODO Create shield colouring
        }
    }
}