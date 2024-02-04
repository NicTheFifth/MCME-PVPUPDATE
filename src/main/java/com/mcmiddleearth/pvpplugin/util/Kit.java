package com.mcmiddleearth.pvpplugin.util;

import org.bukkit.entity.Player;

import java.util.function.Consumer;
import java.util.function.Function;

public class Kit {
    private Consumer<Player> inventory;

    public Kit(Consumer<Player> inventory){
        this.inventory = inventory;
    }

    //<editor-fold defaultstate="collapsed" desc="Getter and Setters">
    public Consumer<Player> getInventory() {
        return this.inventory;
    }
    public void setInventory(Consumer<Player> inventory) {
        this.inventory = inventory;
    }
    //</editor-fold>
}
