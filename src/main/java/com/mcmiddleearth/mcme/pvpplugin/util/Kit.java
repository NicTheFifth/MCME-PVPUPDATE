package com.mcmiddleearth.mcme.pvpplugin.util;

import org.bukkit.entity.Player;
import java.util.function.Function;

public class Kit {
    private Function<Player,Void> inventory;

    public Kit(Function<Player,Void> inventory){
        this.inventory = inventory;
    }

    //<editor-fold defaultstate="collapsed" desc="Getter and Setters">
    public Function<Player,Void> getInventory() {
        return this.inventory;
    }
    public void setInventory(Function<Player,Void> inventory) {
        this.inventory = inventory;
    }
    //</editor-fold>
}
