package com.mcmiddleearth.mcme.pvpplugin.runners.runnerUtil;

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
        if(item instanceof LeatherArmorMeta){
            LeatherArmorMeta newColour = (LeatherArmorMeta) item;
            newColour.setColor(teamColour);
            item.setItemMeta(newColour);
        }
        if(item.getType().equals(Material.SHIELD)){
            ItemMeta meta = item.getItemMeta();
            BlockStateMeta bmeta = (BlockStateMeta) meta;
            Banner banner = (Banner) bmeta.getBlockState();
            banner.setBaseColor(DyeColor.getByColor(teamColour));
            bmeta.setBlockState(banner);
            item.setItemMeta(bmeta);
        }
    }
}
