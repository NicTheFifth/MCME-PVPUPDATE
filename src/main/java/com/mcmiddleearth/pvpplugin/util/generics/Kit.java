package com.mcmiddleearth.pvpplugin.util.generics;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

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
