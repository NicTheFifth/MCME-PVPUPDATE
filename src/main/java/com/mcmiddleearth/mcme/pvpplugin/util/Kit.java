package com.mcmiddleearth.mcme.pvpplugin.util;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;

public class Kit {
    @Getter @Setter
    Inventory inventory;
    public Kit(Inventory inventory){
        this.inventory = inventory;
    }
}
