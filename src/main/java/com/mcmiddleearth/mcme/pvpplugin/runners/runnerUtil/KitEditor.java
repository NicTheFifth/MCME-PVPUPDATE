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
        if(item == null)
            return;
        if(item instanceof LeatherArmorMeta){
            LeatherArmorMeta newColour = (LeatherArmorMeta) item;
            newColour.setColor(teamColour);
            item.setItemMeta(newColour);
        }
        if(item.getType().equals(Material.SHIELD)){
            ItemMeta meta = item.getItemMeta();
            BlockStateMeta bmeta = (BlockStateMeta) meta;
            Banner banner = (Banner) bmeta.getBlockState();
            //TODO:Fix banner colouring.
            DyeColor c = DyeColor.getByColor(teamColour);
            if(c != null)
                banner.setBaseColor(c);
            bmeta.setBlockState(banner);
            item.setItemMeta(bmeta);
        }
    }
    private static DyeColor getDyeColour(Color color){
        switch(color){
            case Color.WHITE :
                return DyeColor.WHITE;
            case Color.GRAY :
                return DyeColor.GRAY;
            case Color.BLACK :
                return DyeColor.BLACK;
            case Color.RED :
                return DyeColor.RED;
            case Color.MAROON :
            case Color.YELLOW :
            case Color.OLIVE :
            case Color.LIME :
            case Color.GREEN :
            case Color.AQUA :
            case Color.TEAL :
            case Color.BLUE :
            case Color.NAVY :
            case Color.FUCHSIA :
            case Color.PURPLE :
            case Color.ORANGE :
        }
    }
}
