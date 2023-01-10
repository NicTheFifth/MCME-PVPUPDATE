package com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.util.Kit;
import com.mcmiddleearth.mcme.pvpplugin.util.Team;
import com.mcmiddleearth.mcme.pvpplugin.runners.runnerUtil.KitEditor;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.function.Function;


public class TeamSlayerRunner extends GamemodeRunner {
    Team red = new Team();
    Team blue = new Team();
    Integer pointLimit;

    @Override
    public boolean canStart() {
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void end(boolean stopped) {

    }

    @Override
    public List<String> tryJoin(Player player) {
        return null;
    }

    @Override
    public void leave(Player player, boolean failedJoin) {

    }

    void initBaseRunner(){

    }
    //<editor-fold desc="Initialising teams">
    private void InitialiseInfected() {
        infected.setPrefix("Infected");
        infected.setTeamColour(Color.RED);
        infected.setKit(InfectedKit());
    }

    private Kit InfectedKit() {
        Function<Player, Void> invFunc = (x -> {
            PlayerInventory returnInventory = x.getInventory();
            returnInventory.clear();
            returnInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            returnInventory.setItemInOffHand(new ItemStack(Material.SHIELD));
            returnInventory.setItem(0, new ItemStack(Material.IRON_SWORD));
            ItemStack bow = new ItemStack(Material.BOW);
            bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
            returnInventory.setItem(1, bow);
            returnInventory.setItem(2, new ItemStack(Material.ARROW));
            returnInventory.forEach(item -> KitEditor.setItemColour(item, infected.getTeamColour()));
            return null;
        });
        return new Kit(invFunc);
    }

    private void InitialiseSurvivors() {
        survivors.setPrefix("Survivor");
        survivors.setTeamColour(Color.BLUE);
        survivors.setKit(SurvivorKit());
    }

    private Kit SurvivorKit() {
        Function<Player, Void> invFunc = (x -> {
            PlayerInventory returnInventory = x.getInventory();
            returnInventory.clear();
            returnInventory.setBoots(new ItemStack(Material.LEATHER_BOOTS));
            returnInventory.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
            returnInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            returnInventory.setHelmet(new ItemStack(Material.LEATHER_HELMET));
            returnInventory.setItemInOffHand(new ItemStack(Material.SHIELD));
            returnInventory.setItem(0, new ItemStack(Material.IRON_SWORD));
            ItemStack bow = new ItemStack(Material.BOW);
            bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
            returnInventory.setItem(1, bow);
            returnInventory.setItem(2, new ItemStack(Material.ARROW));
            returnInventory.forEach(item -> KitEditor.setItemColour(item, survivors.getTeamColour()));
            return null;
        });
        return new Kit(invFunc);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Team getRed() {
        return this.red;
    }
    public Team getBlue() {
        return this.blue;
    }

    public Integer getPointLimit(){
        return pointLimit;
    }

    public void setPointLimit(Integer pointLimit){
        this.pointLimit = pointLimit;
    }
    //</editor-fold>
}
