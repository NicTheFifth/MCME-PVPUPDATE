package com.mcmiddleearth.mcme.pvpplugin.util;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.PlayerInventory;

public class Kit {
    @Getter @Setter
    PlayerInventory inventory;
    public Kit(PlayerInventory inventory){
        this.inventory = inventory;
    }
}
