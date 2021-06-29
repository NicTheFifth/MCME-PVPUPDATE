package com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.transcribers.InfectedTranscriber;
import com.mcmiddleearth.mcme.pvpplugin.runners.runnerUtil.ScoreboardEditor;
import com.mcmiddleearth.mcme.pvpplugin.util.Kit;
import com.mcmiddleearth.mcme.pvpplugin.util.Team;
import com.mcmiddleearth.mcme.pvpplugin.runners.runnerUtil.KitEditor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import static com.mcmiddleearth.mcme.pvpplugin.util.Matchmaker.addMember;

public class InfectedRunner extends BaseRunner {

    @Getter
    Team infected = new Team();

    @Getter
    Team survivors = new Team();

    @Getter@Setter
    Integer timeSec;

    public InfectedRunner(JSONMap map, PVPPlugin pvpplugin, boolean privateGame){
        this.pvpPlugin = pvpplugin;
        this.privateGame = privateGame;
        InfectedTranscriber.Transcribe(map, this);
        InitialiseInfected();
        InitialiseSurvivors();
        gameState = State.QUEUED;
    }

    @Override
    public void Start(){
        if(CanStart()){
            ScoreboardEditor.InitInfected(scoreboard,infected,survivors,timeSec);
            //TODO: Setup when starting
            super.Start();
            run();
        }
    }

    @Override
    public void run() {
        timeSec--;
        if (timeSec == 0) {
            End(false);
        }
        if (timeSec == 1) {
            players.forEach(player -> player.sendMessage(ChatColor.GREEN + "1 second remaining!"));
        }
        if (timeSec >= 2 && timeSec <= 5) {
            players.forEach(player -> player.sendMessage(ChatColor.GREEN + "" + timeSec + "seconds remaining!"));
        }
        if (timeSec == 30) {
            players.forEach(player -> player.sendMessage(ChatColor.GREEN + "30 seconds remaining!"));
        }
        ScoreboardEditor.updateTime(scoreboard, timeSec);
    }

    @Override
    public boolean CanStart(){
        return timeSec != null && super.CanStart();
    }

    @Override
    public void End(boolean stopped){
        super.End(stopped);
    }

    @Override
    public boolean CanJoin(Player player){
        return super.CanJoin(player);
    }

    @Override
    public void Join(Player player) {
        super.Join(player);
        if (gameState != State.QUEUED) {
            addMember(player, infected, survivors);
        }
    }

    @Override
    public void Leave(Player player){
        if(survivors.getMembers().contains(player)){
            survivors.getDeadMembers().add(player);
        }
        infected.getMembers().remove(player);
        super.Leave(player);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }

    private void InitialiseInfected() {
        infected.setPrefix("Infected");
        infected.setTeamColour(Color.RED);
        infected.setKit(InfectedKit());
    }

    private Kit InfectedKit() {
        PlayerInventory returnInventory = (PlayerInventory) Bukkit.createInventory(null, InventoryType.PLAYER);
        returnInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        returnInventory.setItemInOffHand(new ItemStack(Material.SHIELD));
        returnInventory.setItem(0, new ItemStack(Material.IRON_SWORD));
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE,1);
        returnInventory.setItem(1, bow);
        returnInventory.setItem(2, new ItemStack(Material.ARROW));
        returnInventory.forEach(item -> KitEditor.setItemColour(item, infected.getTeamColour()));
        return new Kit(returnInventory);
    }

    private void InitialiseSurvivors() {
        survivors.setPrefix("Survivor");
        survivors.setTeamColour(Color.BLUE);
        survivors.setKit(SurvivorKit());
    }

    private Kit SurvivorKit() {
        PlayerInventory returnInventory = (PlayerInventory) Bukkit.createInventory(null, InventoryType.PLAYER);
        returnInventory.setBoots(new ItemStack(Material.LEATHER_BOOTS));
        returnInventory.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        returnInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        returnInventory.setHelmet(new ItemStack(Material.LEATHER_HELMET));
        returnInventory.setItemInOffHand(new ItemStack(Material.SHIELD));
        returnInventory.setItem(0, new ItemStack(Material.IRON_SWORD));
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE,1);
        returnInventory.setItem(1, bow);
        returnInventory.setItem(2, new ItemStack(Material.ARROW));
        returnInventory.forEach(item -> KitEditor.setItemColour(item, survivors.getTeamColour()));
        return new Kit(returnInventory);
    }
}