package com.mcmiddleearth.mcme.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.transcribers.InfectedTranscriber;
import com.mcmiddleearth.mcme.pvpplugin.util.Kit;
import com.mcmiddleearth.mcme.pvpplugin.util.Team;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class InfectedRunner extends BaseRunner {

    @Getter
    Team infected = new Team();
    @Getter
    Team survivors = new Team();
    @Getter@Setter
    Integer timeMin;
    public InfectedRunner(JSONMap map, PVPPlugin pvpplugin, boolean privateGame){
        this.pvpPlugin = pvpplugin;
        this.privateGame = privateGame;
        InfectedTranscriber transcriber = new InfectedTranscriber();
        transcriber.Transcribe(map, this);
        InitialiseInfected();
        InitialiseSurvivors();
    }

    @Override
    public void Start(){

    }

    @Override
    public void run() {

    }

    @Override
    public boolean CanStart(){
        return timeMin != null && super.CanStart();
    }

    @Override
    public void End(){

    }

    @Override
    public boolean CanJoin(Player player){
        return false;
    }

    @Override
    public void Join(Player player){

    }

    @Override
    public void Leave(Player player){

    }

    private void InitialiseInfected() {
        infected.setPrefix("Infected");
        infected.setTeamColour(Color.RED);
        infected.setKit(InfectedKit());
    }
    //TODO: Create infected Kit
    private Kit InfectedKit() {
        PlayerInventory returnInventory = (PlayerInventory) Bukkit.createInventory(null, InventoryType.PLAYER);
        returnInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        returnInventory.setItemInOffHand(new ItemStack(Material.SHIELD));
        returnInventory.setItem(0, new ItemStack(Material.IRON_SWORD));
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE,1);
        returnInventory.setItem(1, bow);
        returnInventory.setItem(2, new ItemStack(Material.ARROW));
         returnInventory.forEach(item -> SetItemColour(item, infected.getTeamColour()));
        return new Kit(returnInventory);
    }

    private void InitialiseSurvivors() {
        survivors.setPrefix("Survivor");
        survivors.setTeamColour(Color.BLUE);
        survivors.setKit(SurvivorKit());
    }
    //TODO: Create survivor Kit
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
        returnInventory.forEach(item -> SetItemColour(item, survivors.getTeamColour()));
        return new Kit(returnInventory);
    }
    //TODO: Move to Util???
    private void SetItemColour(ItemStack item, Color teamColour) {
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
