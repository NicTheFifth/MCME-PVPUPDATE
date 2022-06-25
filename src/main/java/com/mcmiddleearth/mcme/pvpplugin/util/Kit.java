package com.mcmiddleearth.mcme.pvpplugin.util;

import org.bukkit.inventory.PlayerInventory;

public class Kit {
    PlayerInventory inventory;

    public Kit(PlayerInventory inventory){
        this.inventory = inventory;
    }

    //<editor-fold defaultstate="collapsed" desc="Getter and Setters">
    public PlayerInventory getInventory() {
        return this.inventory;
    }
    public void setInventory(PlayerInventory inventory) {
        this.inventory = inventory;
    }
    //</editor-fold>
}
