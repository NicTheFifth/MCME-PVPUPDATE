package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONInfected;
import com.mcmiddleearth.pvpplugin.json.transcribers.AreaTranscriber;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.KitEditor;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import com.mcmiddleearth.pvpplugin.util.Kit;
import com.mcmiddleearth.pvpplugin.util.Team;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class Infected extends GamemodeRunner {
    int timeLimitSeconds;
    public static int DefaultTimeLimit(){
        return 300;
    }

    Team survivors = new Team();
    Team infected = new Team();

    public Infected(JSONMap map, int timeLimitSeconds){
        region = AreaTranscriber.TranscribeArea(map);
        JSONInfected infected = map.getJSONInfected();
        this.timeLimitSeconds = timeLimitSeconds;
        maxPlayers = infected.getMaximumPlayers();
        mapName = map.getTitle();
        eventListener = new IListener();
        initTeams(map);
        initStartConditions();
        initStartActions();
        initEndActions();
        initJoinConditions();
        initJoinActions();
        initLeaveActions();
    }
    //<editor-fold defaultstate="collapsed" desc="Teams">
    private void initTeams(JSONMap map){
        initSpectator(map.getSpawn());
        initSurvivor(map.getJSONInfected().getSurvivorSpawn());
        initInfected(map.getJSONInfected().getInfectedSpawn());
    }
    private void initSurvivor(JSONLocation survivorSpawn){
        survivors.setPrefix("Survivor");
        survivors.setTeamColour(Color.BLUE);
        survivors.setChatColor(ChatColor.BLUE);
        survivors.setGameMode(GameMode.ADVENTURE);
        survivors.setKit(createSurvivorKit());
        survivors.setSpawnLocations(List.of(LocationTranscriber.TranscribeFromJSON(survivorSpawn)));

    }
    private Kit createSurvivorKit(){
        Consumer<Player> invFunc = (player -> {
            createInfectedKit(Color.BLUE).accept(player);
            PlayerInventory returnInventory = player.getInventory();
            returnInventory.setHelmet(new ItemStack(Material.LEATHER_HELMET));
            returnInventory.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
            returnInventory.setBoots(new ItemStack(Material.LEATHER_BOOTS));
            returnInventory.setItemInOffHand(new ItemStack(Material.SHIELD));
            returnInventory.forEach(item -> KitEditor.setItemColour(item,
                    Color.BLUE));
        });
        return new Kit(invFunc);

    }
    private void initInfected(JSONLocation infectedSpawn){
        infected.setPrefix("Infected");
        infected.setTeamColour(Color.RED);
        infected.setChatColor(ChatColor.RED);
        infected.setGameMode(GameMode.ADVENTURE);
        //TODO: Fix the infected kit to be slightly stronger
        infected.setKit(new Kit(createInfectedKit(Color.RED)));
        infected.setSpawnLocations(List.of(LocationTranscriber.TranscribeFromJSON(infectedSpawn)));
    }
    private @NotNull Consumer<Player> createInfectedKit(Color color){
        return (x -> {
            PlayerInventory returnInventory = x.getInventory();
            returnInventory.clear();
            returnInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            returnInventory.setItem(0, new ItemStack(Material.IRON_SWORD));
            ItemStack bow = new ItemStack(Material.BOW);
            bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
            returnInventory.setItem(1, bow);
            returnInventory.setItem(2, new ItemStack(Material.ARROW));
            returnInventory.forEach(item -> KitEditor.setItemColour(item,
                    color));
        });
    }
    //</editor-fold>
    @Override
    protected void initStartConditions() {

    }

    @Override
    protected void initStartActions() {

    }

    @Override
    protected void initEndActions() {

    }

    @Override
    protected void initJoinConditions() {

    }

    @Override
    protected void initJoinActions() {

    }

    @Override
    protected void initLeaveActions() {

    }

    @Override
    public String getGamemode() {
        return Gamemodes.INFECTED;
    }
    public class IListener extends GamemodeListener{
        public IListener(){
            initOnPlayerDeathActions();
        }
        @Override
        protected void initOnPlayerDeathActions() {

        }
    }
}
